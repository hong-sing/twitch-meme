package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.post.Youtube;
import com.ewok.twitchmeme.domain.post.YoutubeRepository;
import com.ewok.twitchmeme.dto.PostSaveRequestDto;
import com.ewok.twitchmeme.dto.PostUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    YoutubeRepository youtubeRepository;

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
        List<Youtube> youtubes = youtubeRepository.findAll();
        assertThat(title).isEqualTo(findPost.getTitle());
        assertThat(findPost.getYoutubes().get(0).getLink()).contains("youtube1");
        assertThat(youtubes.get(0).getLink()).contains("youtube1");
    }

    @Test
    void 게시글_수정() {
        //given
        List<Youtube> youtubes = new ArrayList<>();
        Post post = Post.builder()
                        .title("제목")
                        .summary("요약")
                        .content("내용")
                        .youtubes(youtubes)
                        .broadcastId("aaa1234")
                        .build();
        Post savePost = postRepository.save(post);
        ArrayList<String> reference = new ArrayList<>();
        PostUpdateRequestDto updateRequestDto = PostUpdateRequestDto.builder()
                                                                    .title("수정된 제목")
                                                                    .summary("수정된 요약")
                                                                    .reference(reference)
                                                                    .content("내용")
                                                                    .build();

        //when
        postService.update(savePost.getId(), updateRequestDto);

        //then
        Post findPost = postRepository.findById(savePost.getId()).get();
        assertThat("수정된 제목").isEqualTo(findPost.getTitle());
    }

    @Test
    void 게시글_삭제() {
        //given
        Post post = Post.builder()
                .title("제목")
                .summary("요약")
                .content("내용")
                .broadcastId("aaa1234")
                .build();
        Post savePost = postRepository.save(post);

        //when
        postService.delete(savePost.getId());

        //that
        assertThat(postRepository.findById(savePost.getId())).isEmpty();

    }
}