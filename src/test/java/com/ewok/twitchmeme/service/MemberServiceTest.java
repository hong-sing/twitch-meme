package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.post.Reply;
import com.ewok.twitchmeme.domain.post.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .picture("이미지")
                .role(Role.USER)
                .nickname("닉네임")
                .twitchId(999L)
                .build();

        memberRepository.save(member);
    }


    @Test
    void 회원조회() {
        //when
        List<Member> list = memberRepository.findAll();

        //then
        assertThat(list.get(0).getNickname()).isEqualTo("닉네임");
        assertThat(list.get(0).getPicture()).isEqualTo("이미지");
    }

    @Test
    void 회원탈퇴() {
        //given
        Long memberId = memberRepository.findByTwitchId(999L).get().getId();


        //when
        memberService.delete(memberId);

        //then
        System.out.println(memberRepository.findById(memberId));
    }

    @Test
    void 회원탈퇴_작성글이_있는_경우() {
        //given
        Member member1 = memberRepository.findByTwitchId(999L).get();
        post = Post.builder()
                .title("제목")
                .summary("요약")
                .content("내용")
                .broadcastId("aaa123")
                .member(member1)
                .build();
        Post post1 = postRepository.save(post);
        member.addPost(post1);

        //when
        memberService.delete(member1.getId());

        //then
        assertThat(postRepository.findById(post1.getId())).isEmpty();
        assertThat(memberRepository.findById(member1.getId())).isEmpty();
    }

    @Test
    void 회원탈퇴_작성글존재_댓글존재() {
        //given
        Member member1 = memberRepository.findByTwitchId(999L).get();
        post = Post.builder()
                .title("제목")
                .summary("요약")
                .content("내용")
                .broadcastId("aaa123")
                .member(member1)
                .build();
        Post post1 = postRepository.save(post);

        List<Reply> replies = new ArrayList<>();
        Reply reply = Reply.builder()
                .post(post1)
                .member(member)
                .replies(replies)
                .content("댓글내용")
                .build();
        Reply reply1 = replyRepository.save(reply);

        member.addPost(post1);
        member.addReply(reply1);

        //when
        memberService.delete(member1.getId());

        //then
        assertThat(postRepository.findById(post1.getId())).isEmpty();
        assertThat(memberRepository.findById(member1.getId())).isEmpty();
        assertThat(replyRepository.findById(reply1.getId())).isEmpty();
    }

}