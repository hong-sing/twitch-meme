package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.post.Good;
import com.ewok.twitchmeme.domain.post.Post;
import lombok.Getter;

@Getter
public class GoodRequestDto {

    private Long postId;
    private Long memberId;

    public Good toEntity(Post post, Member member) {
        return Good.builder()
                .post(post)
                .member(member)
                .build();
    }
}
