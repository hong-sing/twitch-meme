package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.post.Good;
import com.ewok.twitchmeme.domain.post.GoodRepository;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.dto.GoodRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Long cancel(GoodRequestDto requestDto) {
        Good findGood = goodRepository.findByPostIdAndMemberId(requestDto.getPostId(), requestDto.getMemberId());
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        List<Good> goods = post.getGoods();
        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).getMember().getId().equals(requestDto.getMemberId())) {
                goods.remove(i);
            }
        }
        goodRepository.delete(findGood);
        return post.getId();
    }
}
