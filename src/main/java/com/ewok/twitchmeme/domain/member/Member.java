package com.ewok.twitchmeme.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;
    
    @Column
    private Long twitchId;  //트위치 자체id
    
    @Column
    private String nickname;    //트위치 닉네임
    
    @Column
    private String picture;     //트위치 프로필 이미지

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Builder
    public Member(Long id, Long twitchId, String nickname, String picture, Role role) {
        this.id = id;
        this.twitchId = twitchId;
        this.nickname = nickname;
        this.picture = picture;
        this.role = role;
    }

    public Member update(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
