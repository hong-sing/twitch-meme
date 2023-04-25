package com.ewok.twitchmeme.domain.post;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class Youtube {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column
    private String link;

    @Builder
    public Youtube(Post post, String link) {
        this.post = post;
        this.link = link;
    }
}