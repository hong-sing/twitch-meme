package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Good;
import com.ewok.twitchmeme.domain.post.GoodRepository;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.dto.GoodRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class GoodServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GoodRepository goodRepository;

    @Autowired
    private GoodService goodService;

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
    void 좋아요_추가() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        GoodRequestDto requestDto = GoodRequestDto.builder()
                                            .postId(list.get(0).getId())
                                            .memberId(member.getId())
                                            .build();

        //when
        Long id = goodService.add(requestDto);

        //then
        Good findGood = goodRepository.findById(id).get();
        assertThat(findGood.getPost()).isEqualTo(list.get(0));
    }

}