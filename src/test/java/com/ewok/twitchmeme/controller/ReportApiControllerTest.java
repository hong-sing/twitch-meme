package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.report.Report;
import com.ewok.twitchmeme.domain.report.ReportReposiroty;
import com.ewok.twitchmeme.dto.report.ReportRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReportReposiroty reportReposiroty;

    private MockMvc mvc;

    private Post post;

    private Member member;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

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
    @WithMockUser(roles = "USER")
    void 신고_등록() throws Exception {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        ReportRequestDto requestDto = ReportRequestDto.builder()
                .postId(list.get(0).getId())
                .memberId(member.getId())
                .reportReason("신고합니다.")
                .build();

        String url = "http://localhost:" + port + "/api/v1/report";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Report> all = reportReposiroty.findAll();
        assertThat(all.get(0).getReason()).isEqualTo("신고합니다.");
    }
}