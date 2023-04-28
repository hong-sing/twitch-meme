package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.post.Good;
import com.ewok.twitchmeme.domain.post.GoodRepository;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.dto.GoodRequestDto;
import com.ewok.twitchmeme.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GoodService {

    private final PostRepository postRepository;
    private final GoodRepository goodRepository;
    private final MemberRepository memberRepository;

    /** 좋아요 */
    public Long add(GoodRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        Good good = goodRepository.save(requestDto.toEntity(post, member));
        post.addGood(good);
        member.addGood(good);
        return good.getId();
    }

    /** 좋아요 취소 */
    public Long cancel(GoodRequestDto requestDto) {
        Good findGood = goodRepository.findByPostIdAndMemberId(requestDto.getPostId(), requestDto.getMemberId());
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        List<Good> goods = post.getGoods();

        //연관관계 제거
        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).getMember().getId().equals(requestDto.getMemberId())) {    //해당 게시글의 goods 필드 중 유저의 좋아요만 제거
                goods.remove(i);
            }
        }
        goodRepository.delete(findGood);
        return post.getId();
    }

    public List<PostResponseDto> findByMemberId(Long memberId) {
        List<Good> goods = goodRepository.findByMemberId(memberId);
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        for (Good good : goods) {
            PostResponseDto dto = new PostResponseDto(good.getPost());
            responseDtoList.add(dto);
        }
        return responseDtoList;
    }
}
