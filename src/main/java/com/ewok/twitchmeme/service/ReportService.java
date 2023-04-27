package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.report.Report;
import com.ewok.twitchmeme.domain.report.ReportReposiroty;
import com.ewok.twitchmeme.dto.report.ReportRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportReposiroty reportReposiroty;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Long save(ReportRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

        Report saveReport = reportReposiroty.save(Report.builder()
                                                        .post(post)
                                                        .member(member)
                                                        .reason(requestDto.getReportReason())
                                                        .build());
        return saveReport.getId();
    }
}
