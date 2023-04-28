package com.ewok.twitchmeme.domain.post;

import com.ewok.twitchmeme.domain.BaseTimeEntity;
import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.report.Report;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)   //Post에서만 youtube 참조
    private List<Youtube> youtubes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) //Member에서도 참조
    private List<Good> goods = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)   //Post에서만 Report 참조
    private List<Report> reports = new ArrayList<>();


    public Post(String title, String summary, String broadcastId, String content, Member member) {
        this.title = title;
        this.summary = summary;
        this.broadcastId = broadcastId;
        this.content = content;
        this.member = member;
    }

    @Builder
    public Post(String title, String summary, String broadcastId, String content, Member member, List<Youtube> youtubes, List<Good> goods) {
        this.title = title;
        this.summary = summary;
        this.broadcastId = broadcastId;
        this.content = content;
        this.member = member;
        this.youtubes = youtubes;
        this.goods = goods;
    }

    public void update(String title, String summary, String content, List<Youtube> youtubes) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.youtubes = youtubes;
    }

    //연관관계 메서드
    public void addYoutube(Youtube youtube) {
        this.youtubes.add(youtube);
    }

    public void addGood(Good good) {
        this.goods.add(good);
    }

    public void addReport(Report report) {
        this.reports.add(report);
    }
}
