package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.report.ReportRequestDto;
import com.ewok.twitchmeme.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReportApiController {

    private final ReportService reportService;
    @PostMapping("/api/v1/report")
    public Long save(@RequestBody ReportRequestDto requestDto) {
        return reportService.save(requestDto);
    }
}
