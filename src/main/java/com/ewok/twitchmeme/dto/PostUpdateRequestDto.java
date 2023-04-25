package com.ewok.twitchmeme.dto;

import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.Youtube;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class PostUpdateRequestDto {

    private String title;
    private String summary;
    private String content;
    private ArrayList<String> reference;

    public Youtube toEntity(Post post, String link) {
        return Youtube.builder()
                .post(post)
                .link(link)
                .build();
    }
}
