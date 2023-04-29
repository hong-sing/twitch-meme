package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributesKey;
    private String nickName;
    private String sub;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributesKey, String nickName, String sub, String picture) {
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.nickName = nickName;
        this.sub = sub;
        this.picture = picture;
    }

    public static OAuthAttributes ofTwitch(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributesKey(userNameAttributeName)
                .nickName((String) attributes.get("preferred_username"))
                .sub((String) attributes.get("sub"))
                .picture((String) attributes.get("picture"))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(nickName)
                .twitchId(Long.parseLong(sub))
                .role(Role.USER)
                .picture(picture)
                .build();
    }

}
