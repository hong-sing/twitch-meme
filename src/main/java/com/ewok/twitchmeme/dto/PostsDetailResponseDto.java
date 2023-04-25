package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.post.Post;
import lombok.Getter;

@Getter
public class PostsDetailResponseDto {

    private Long id;
    private Member member;
    private String title;
    private String summary;
    private String content;
    private String broadcastId;

    public PostsDetailResponseDto(Post post) {
        this.id = post.getId();
        this.member = post.getMember();
        this.title = post.getTitle();
        this.summary = post.getSummary();
        this.content = post.getContent();
        this.broadcastId = post.getBroadcastId();
    }
}
