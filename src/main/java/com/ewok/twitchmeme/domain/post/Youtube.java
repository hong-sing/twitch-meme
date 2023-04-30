package com.ewok.twitchmeme.domain.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Youtube {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
