package com.ewok.twitchmeme.domain.member;

import com.ewok.twitchmeme.domain.BaseTimeEntity;
import com.ewok.twitchmeme.domain.post.Good;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.Reply;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;
    
    @Column
    private Long twitchId;  //트위치 자체id
    
    @Column
    private String nickname;    //트위치 닉네임
    
    @Column
    private String picture;     //트위치 프로필 이미지

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;  //USER, ADMIN

    //회원탈퇴 -> 작성한 게시글, 댓글, 좋아요 표시 모두 삭제
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Good> goodList = new ArrayList<>();

    @Builder
    public Member(Long id, Long twitchId, String nickname, String picture, Role role) {
        this.id = id;
        this.twitchId = twitchId;
        this.nickname = nickname;
        this.picture = picture;
        this.role = role;
    }

    public Member update(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }


    //연관관계 메서드
    public void addPost(Post post) {
        this.postList.add(post);
    }

    public void addReply(Reply reply) {
        this.replyList.add(reply);
    }

    public void addGood(Good good) {
        this.goodList.add(good);
    }
}
