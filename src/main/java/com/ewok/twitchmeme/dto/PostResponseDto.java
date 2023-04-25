package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.Youtube;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private Member member;
    private String summary;
    private List<Youtube> youtube;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.member = post.getMember();
        this.summary = post.getSummary();
        this.youtube = post.getYoutubes();
        this.modifiedDate = post.getModifiedDate();
    }
}
