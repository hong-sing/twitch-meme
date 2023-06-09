package com.ewok.twitchmeme.dto.post;

import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.Youtube;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
public class PostUpdateRequestDto {

    private String title;
    private String summary;
    private String content;
    private ArrayList<String> reference;


    @Builder
    public PostUpdateRequestDto(String title, String summary, String content, ArrayList<String> reference) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.reference = reference;
    }

    public Youtube toEntity(Post post, String link) {
        return Youtube.builder()
                .post(post)
                .link(link)
                .build();
    }
}
