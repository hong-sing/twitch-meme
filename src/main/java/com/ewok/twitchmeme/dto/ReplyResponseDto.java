package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.post.Reply;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReplyResponseDto {

    private Long id;
    private Member member;
    private String content;
    private Character remove;
    private Reply parentReply;
    private LocalDateTime modifiedDate;
    private List<Reply> replies;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.member = reply.getMember();
        this.content = reply.getContent();
        this.remove = reply.getRemove();
        this.modifiedDate = reply.getModifiedDate();
        this.parentReply = reply.getParent();
        this.replies = reply.getReplies();
    }
}
