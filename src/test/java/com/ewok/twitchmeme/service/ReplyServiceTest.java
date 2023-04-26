package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.post.ReplyRepository;
import com.ewok.twitchmeme.dto.ReplyRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReplyServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ReplyService replyService;

    private Post post;
    private Member member;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .title("제목")
                .summary("요약")
                .content("내용")
                .broadcastId("aaa123")
                .build();

        member = Member.builder()
                .picture("이미지")
                .role(Role.USER)
                .nickname("닉네임")
                .twitchId(999L)
                .build();
        postRepository.save(post);
        memberRepository.save(member);
    }

    @Test
    void 댓글_등록() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        ReplyRequestDto requestDto = ReplyRequestDto.builder()
                .comment("댓글")
                .postId(list.get(0).getId())
                .memberId(member.getId())
                .build();

        //when
        Long id = replyService.save(requestDto);

        //then
        assertThat(replyRepository.findById(id).get().getContent()).isEqualTo("댓글");
    }
}