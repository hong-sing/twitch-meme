package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class SessionMember implements Serializable {

    private Long id;
    private String nickname;
    private String picture;
    private Role role;
    public SessionMember(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.picture = member.getPicture();
        this.role = member.getRole();
    }
}
