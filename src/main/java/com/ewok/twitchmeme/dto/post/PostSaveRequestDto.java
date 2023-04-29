package com.ewok.twitchmeme.dto.post;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.Youtube;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private Long memberId;
    private String title;
    private String summary;
    private String broadcastId;
    private String content;
    private ArrayList<String> reference;

    @Builder
    public PostSaveRequestDto(Long memberId, String title, String summary, String broadcastId, String content, ArrayList<String> reference) {
        this.memberId = memberId;
        this.title = title;
        this.summary = summary;
        this.broadcastId = broadcastId;
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
