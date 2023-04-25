package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.post.Good;
import com.ewok.twitchmeme.domain.post.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostsDetailResponseDto {

    private Long id;
    private Member member;
    private String title;
    private String summary;
    private String content;
    private String broadcastId;
    private boolean checkGood;

    public PostsDetailResponseDto(Post post, boolean checkGood) {
        this.id = post.getId();
        this.member = post.getMember();
        this.title = post.getTitle();
        this.summary = post.getSummary();
        this.content = post.getContent();
        this.broadcastId = post.getBroadcastId();
        this.checkGood = checkGood;
    }
}
