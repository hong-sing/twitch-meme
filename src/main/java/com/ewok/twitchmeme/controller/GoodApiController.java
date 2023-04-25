package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.GoodRequestDto;
import com.ewok.twitchmeme.service.GoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GoodApiController {

    private final GoodService goodService;

    @PostMapping("/api/v1/good")
    public Long save(@RequestBody GoodRequestDto requestDto) {
        return goodService.add(requestDto);
    }
}
