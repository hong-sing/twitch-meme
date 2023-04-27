package com.ewok.twitchmeme.dto.report;

import com.ewok.twitchmeme.domain.report.Report;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReportRequestDto {

    private Long postId;
    private Long memberId;
    private String reportReason;

    @Builder
    public ReportRequestDto(Long postId, Long memberId, String reportReason) {
        this.postId = postId;
        this.memberId = memberId;
        this.reportReason = reportReason;
    }
}
