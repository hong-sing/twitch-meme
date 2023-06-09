package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.token.Token;
import com.ewok.twitchmeme.domain.token.TokenRepository;
import com.ewok.twitchmeme.dto.*;
import com.ewok.twitchmeme.dto.twitch.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TwitchService {

    @Value("${spring.security.oauth2.client.registration.twitch.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.twitch.client-secret}")
    private String clientSecret;

    private final TokenRepository tokenRepository;

    private final MemberRepository memberRepository;

    private final AccessToken accessToken;

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

    public Streamer getStreamerInfo(String login) {

        // 토큰이 유효하지 않다면 재발급
        String token = getAccessToken();
        if (!isAccessTokenValid(token)) {
            token = reGetAccessToken();
        }

        // 정보 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Client-Id", clientId);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.twitch.tv/helix/users")
                .queryParam("login", login);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, LinkedHashMap.class);
        LinkedHashMap data = response.getBody();
        ArrayList list = (ArrayList) data.get("data");
        LinkedHashMap info = (LinkedHashMap) list.get(0);
        Streamer streamer = new Streamer(info);
        return streamer;
    }

    public List<StreamInfoData> getStreamInfo(String language) {
        // 토큰이 유효하지 않다면 재발급
        String token = getAccessToken();
        if (!isAccessTokenValid(token)) {
            token = reGetAccessToken();
        }

        // 정보 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Client-Id", clientId);
        headers.setBearerAuth(token);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.twitch.tv/helix/streams")
                .queryParam("language", language)
                .queryParam("first", 99);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, LinkedHashMap.class);
        LinkedHashMap data = response.getBody();

        StreamInfo streamInfo = new StreamInfo(data);
        List<StreamInfoData> infoData = streamInfoToStreamInfoData(streamInfo);

        return infoData;
    }


     private List<StreamInfoData> streamInfoToStreamInfoData(StreamInfo streamInfo) {
        List<StreamInfoData> list = new ArrayList<>();
        int size = streamInfo.getData().size();
        for (int i = 0; i < size; i++) {
            ArrayList arrayList = streamInfo.getData();
            LinkedHashMap map = (LinkedHashMap) arrayList.get(i);
            StreamInfoData infoData = new StreamInfoData(map);
            list.add(infoData);
        }
        return list;
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


    public List<FollowInfoData> getFollowList(Long memberId) {
        Long twitchId = memberRepository.findById(memberId).get().getTwitchId();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getAccessToken());
        headers.set("Client-Id", clientId);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.twitch.tv/helix/channels/followed")
                .queryParam("user_id", twitchId)
                .queryParam("first", 100);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, LinkedHashMap.class);
        LinkedHashMap data = response.getBody();

        FollowInfo followInfo = new FollowInfo(data);
        List<FollowInfoData> infoData = followInfoToFollowInfoData(followInfo);
        return infoData;
    }

    private List<FollowInfoData> followInfoToFollowInfoData(FollowInfo followInfo) {
        List<FollowInfoData> list = new ArrayList<>();
        int size = followInfo.getData().size();
        for (int i = 0; i < size; i++) {
            ArrayList arrayList = followInfo.getData();
            LinkedHashMap map = (LinkedHashMap) arrayList.get(i);
            FollowInfoData infoData = new FollowInfoData(map);
            list.add(infoData);
        }
        return list;
    }

    public List<StreamerFollowInfoData> getStreamerFollowList(String streamerId) {
        String accessToken = getAccessToken();
        if (!isAccessTokenValid(accessToken)) {
            accessToken = reGetAccessToken();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Client-Id", clientId);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.twitch.tv/helix/users/follows")
                .queryParam("from_id", streamerId)
                .queryParam("first", 100);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, LinkedHashMap.class);
        LinkedHashMap data = response.getBody();

        StreamerFollowInfo followInfo = new StreamerFollowInfo(data);
        List<StreamerFollowInfoData> infoData = streamerFollowInfoToStreamerFollowInfoData(followInfo);
        return infoData;
    }

    private List<StreamerFollowInfoData> streamerFollowInfoToStreamerFollowInfoData(StreamerFollowInfo followInfo) {
        List<StreamerFollowInfoData> list = new ArrayList<>();
        int size = followInfo.getData().size();
        for (int i = 0; i < size; i++) {
            ArrayList arrayList = followInfo.getData();
            LinkedHashMap map = (LinkedHashMap) arrayList.get(i);
            StreamerFollowInfoData infoData = new StreamerFollowInfoData(map);
            list.add(infoData);
        }
        return list;
    }
}
