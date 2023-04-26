package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.Reply;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReplyRequestDto {

    private String comment;
    private Long postId;
    private Long memberId;
    private Long parentId;

    @Builder
    public ReplyRequestDto(String comment, Long postId, Long memberId, Long parentId) {
        this.comment = comment;
        this.postId = postId;
        this.memberId = memberId;
        this.parentId = parentId;
    }

    public Reply toEntity(Post post, Member member) {
        return Reply.builder()
                .content(comment)
                .post(post)
                .member(member)
                .build();
    }

    public Reply toEntity(Post post, Member member, Reply reply) {
        return Reply.builder()
                .post(post)
                .member(member)
                .content(comment)
                .parent(reply)
                .build();
    }
}
