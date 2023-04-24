package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.token.Token;
import com.ewok.twitchmeme.domain.token.TokenRepository;
import com.ewok.twitchmeme.dto.AccessTokenResponse;
import com.ewok.twitchmeme.dto.ChannelData;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.descriptor.web.JspConfigDescriptorImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TwitchService {

    @Value("${spring.security.oauth2.client.registration.twitch.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.twitch.client-secret}")
    private String clientSecret;

    private final TokenRepository tokenRepository;

    public List<ChannelData> getChannelSearchResult(String streamer) {
        String accessToken = getAccessToken();
        if (!isAccessTokenValid(accessToken)) {
            accessToken = reGetAccessToken();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Client-Id", clientId);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        URI uri = UriComponentsBuilder.fromHttpUrl("https://api.twitch.tv/helix/search/channels")
                .queryParam("query", streamer)
                .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, LinkedHashMap.class);
        LinkedHashMap data = response.getBody();
        ArrayList list = (ArrayList) data.get("data");
        List<ChannelData> channelData = channelListToChannelData(list);
        return channelData;
    }

    private List<ChannelData> channelListToChannelData(ArrayList list) {
        List<ChannelData> channelDataList = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            LinkedHashMap map = (LinkedHashMap) list.get(i);
            ChannelData data = new ChannelData(map);
            channelDataList.add(data);
        }
        return channelDataList;
    }

    private String getAccessToken() {
        Token token = tokenRepository.findById(1l).orElseThrow(() -> new IllegalArgumentException("AccessToken이 없습니다."));
        return token.getToken();
    }

    public boolean isAccessTokenValid(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://id.twitch.tv/oauth2/validate",
                    HttpMethod.GET,
                    entity,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                throw new RuntimeException("Unexpected response from Twitch API");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return false;
            } else {
                throw new RuntimeException("Unexpected error from Twitch API", e);
            }
        }
    }

    @Transactional
    public String reGetAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://id.twitch.tv/oauth2/token")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("grant_type", "client_credentials");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, AccessTokenResponse.class);
        String newToken = response.getBody().getAccess_token();

        // 재발급 받은 토큰 update
        Token tokenEntity = tokenRepository.findById(1l).orElseThrow(() -> new IllegalArgumentException("토큰이 없습니다"));
        tokenEntity.update(newToken);

        return newToken;
    }
}
