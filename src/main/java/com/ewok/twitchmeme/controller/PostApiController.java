package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.PostSaveRequestDto;
import com.ewok.twitchmeme.dto.PostUpdateRequestDto;
import com.ewok.twitchmeme.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    @PostMapping("/api/v1/post")
    public Long save(@RequestBody PostSaveRequestDto postSaveRequestDto) {
//        System.out.println(postSaveRequestDto.getReference());
        return postService.save(postSaveRequestDto);
    }

    @PutMapping("/api/v1/post/{postId}")
    public Long update(@RequestBody PostUpdateRequestDto updateRequstDto, @PathVariable Long postId) {
        return postService.update(postId, updateRequstDto);
    }
}
