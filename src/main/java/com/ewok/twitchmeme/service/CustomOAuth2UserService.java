package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.dto.OAuthAttributes;
import com.ewok.twitchmeme.dto.SessionMember;
import com.ewok.twitchmeme.dto.twitch.AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    private final AccessToken accessToken;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.ofTwitch(userNameAttributeName, oAuth2User.getAttributes());

        accessToken.setAccessToken(userRequest.getAccessToken().getTokenValue());

        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("member", new SessionMember(member));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributesKey());
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByTwitchId(Long.parseLong(attributes.getSub()))
                .map(entity -> entity.update(attributes.getNickName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }

}
