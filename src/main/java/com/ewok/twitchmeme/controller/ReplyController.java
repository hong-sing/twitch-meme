package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.ReplyRequestDto;
import com.ewok.twitchmeme.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/api/v1/reply")
    public Long save(@RequestBody ReplyRequestDto requestDto) {
        return replyService.save(requestDto);
    }

    @PutMapping("/api/v1/reply/{replyId}")
    public Long updateRemoveY(@PathVariable Long replyId) {
        return replyService.updateRemoveY(replyId);
    }
}
