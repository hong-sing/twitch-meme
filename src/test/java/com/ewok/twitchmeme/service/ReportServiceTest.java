package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.report.Report;
import com.ewok.twitchmeme.domain.report.ReportReposiroty;
import com.ewok.twitchmeme.dto.report.ReportRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReportServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportReposiroty reportReposiroty;

    private Post post;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .picture("이미지")
                .role(Role.USER)
                .nickname("닉네임")
                .twitchId(999L)
                .build();

        post = Post.builder()
                .title("제목")
                .summary("요약")
                .content("내용")
                .broadcastId("aaa123")
                .member(member)
                .build();

        postRepository.save(post);
        memberRepository.save(member);
    }

    @Test
    void 신고_등록() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        ReportRequestDto requestDto = ReportRequestDto.builder()
                .postId(list.get(0).getId())
                .memberId(member.getId())
                .reportReason("신고합니다.")
                .build();

        //when
        Long savedReportId = reportService.save(requestDto);

        //then
        assertThat(reportReposiroty.findById(savedReportId).get().getReason()).isEqualTo("신고합니다.");
    }
}