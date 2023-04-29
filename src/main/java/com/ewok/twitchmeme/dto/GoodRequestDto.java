package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.post.Good;
import com.ewok.twitchmeme.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoodRequestDto {

    private Long postId;
    private Long memberId;


    @Builder
    public GoodRequestDto(Long postId, Long memberId) {
        this.postId = postId;
        this.memberId = memberId;
    }

    public Good toEntity(Post post, Member member) {
        return Good.builder()
                .post(post)
                .member(member)
                .build();
    }
}
