package com.ewok.twitchmeme.domain.post;

import com.ewok.twitchmeme.domain.BaseTimeEntity;
import com.ewok.twitchmeme.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.result.UpdateCountOutput;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@DynamicInsert
@Entity
public class Reply extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REPLY_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    @ColumnDefault("'N'")
    private Character remove;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Reply parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public Reply(Post post, Member member, String content, Character remove, Reply parent, List<Reply> replies) {
        this.post = post;
        this.member = member;
        this.content = content;
        this.remove = remove;
        this.parent = parent;
        this.replies = replies;
    }

    public void update(Character remove) {
        this.remove = remove;
    }

    public void update(String content) {
        this.content = content;
    }

    //연관관계 메서드
    public void addReply(Reply reply) {
        this.replies.add(reply);
    }
}
