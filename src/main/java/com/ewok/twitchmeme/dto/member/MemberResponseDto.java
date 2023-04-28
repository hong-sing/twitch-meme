package com.ewok.twitchmeme.dto.member;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberResponseDto {

    private Long id;
    private Long twitchId;
    private String nickname;
    private String picture;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.twitchId = member.getTwitchId();
        this.nickname = member.getNickname();
        this.picture = member.getPicture();
        this.role = member.getRole();
        this.createdDate = member.getCreatedDate();
        this.modifiedDate = member.getModifiedDate();
    }

}
