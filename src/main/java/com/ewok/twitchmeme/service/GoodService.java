package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.post.GoodRepository;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.dto.GoodRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoodService {

    private final PostRepository postRepository;
    private final GoodRepository goodRepository;
    private final MemberRepository memberRepository;

    public Long add(GoodRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        return goodRepository.save(requestDto.toEntity(post, member)).getId();
    }
}
