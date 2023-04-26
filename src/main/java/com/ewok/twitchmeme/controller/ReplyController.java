package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.ReplyRequestDto;
import com.ewok.twitchmeme.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/api/v1/reply")
    public Long save(@RequestBody ReplyRequestDto requestDto) {
        return replyService.save(requestDto);
    }
}
