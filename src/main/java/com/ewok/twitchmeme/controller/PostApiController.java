package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.PostSaveRequestDto;
import com.ewok.twitchmeme.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    @PostMapping("/api/v1/post")
    public Long save(@RequestBody PostSaveRequestDto postSaveRequestDto) {
//        System.out.println(postSaveRequestDto.getReference());
        return postService.save(postSaveRequestDto);
    }
}
