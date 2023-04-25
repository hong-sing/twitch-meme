package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.dto.PostSaveRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    private Member member;

    @BeforeEach
    public void 회원정보() {
         member = Member.builder()
                 .twitchId(111l)
                 .picture("사진경로")
                 .nickname("닉네임")
                 .role(Role.USER)
                 .build();
        memberRepository.save(member);
    }

    @Test
    void 게시글_등록() {
        //given
        Long memberId = memberRepository.findByTwitchId(member.getTwitchId()).get().getId();
        String title = "제목";
        String summary = "요약";
        String content = "내용";
        String broadcastId = "aaa1234";
        ArrayList<String> reference = new ArrayList<>();
        reference.add("http://www.youtube1");
        reference.add("http://www.youtube2");
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                                                        .memberId(memberId)
                                                        .title(title)
                                                        .summary(summary)
                                                        .broadcastId(broadcastId)
                                                        .content(content)
                                                        .reference(reference)
                                                        .build();

        //when
        Long postId = postService.save(requestDto);

        //then
        Post findPost = postRepository.findById(postId).get();
        assertThat(title).isEqualTo(findPost.getTitle());
    }
}