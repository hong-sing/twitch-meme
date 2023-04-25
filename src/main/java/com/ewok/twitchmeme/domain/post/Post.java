package com.ewok.twitchmeme.domain.post;

import com.ewok.twitchmeme.domain.BaseTimeEntity;
import com.ewok.twitchmeme.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "POST_ID")
    private Long id;

    @Column
    private String title;

    @Column
    private String summary;

    @Column
    private String broadcastId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Post(String title, String summary, String broadcastId, String content, Member member) {
        this.title = title;
        this.summary = summary;
        this.broadcastId = broadcastId;
        this.content = content;
        this.member = member;
    }
}
