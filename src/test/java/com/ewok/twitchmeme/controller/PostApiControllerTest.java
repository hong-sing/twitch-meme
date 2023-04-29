package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.post.Youtube;
import com.ewok.twitchmeme.dto.post.PostSaveRequestDto;
import com.ewok.twitchmeme.dto.post.PostUpdateRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MemberRepository memberRepository;

    private MockMvc mvc;

    private Member member;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        member = Member.builder()
                .twitchId(111l)
                .picture("사진경로")
                .nickname("닉네임")
                .role(Role.USER)
                .build();
        memberRepository.save(member);
    }

    @Test
    @WithMockUser(roles = "USER")   // 1
    public void 게시글_등록() throws Exception {
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

        String url = "http://localhost:" + port + "/api/v1/post";

        //when
        mvc.perform(post(url)   // 2
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles = "USER")
    void 게시글_수정() throws Exception {
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
        String url = "http://localhost:" + port + "/api/v1/post/" + savePost.getId();

        //when
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());

        //then
        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo("수정된 제목");
    }

    @Test
    @WithMockUser(roles = "USER")
    void 게시글_삭제() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .summary("요약")
                .content("내용")
                .broadcastId("aaa1234")
                .build();
        Post savePost = postRepository.save(post);
        String url = "http://localhost:" + port + "/api/v1/post/" + savePost.getId();

        //when
        mvc.perform(delete(url)).andExpect(status().isOk());

        //that
        assertThat(postRepository.findById(savePost.getId())).isEmpty();

    }
}