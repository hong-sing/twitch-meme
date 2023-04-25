package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.post.Youtube;
import com.ewok.twitchmeme.domain.post.YoutubeRepository;
import com.ewok.twitchmeme.dto.PostResponseDto;
import com.ewok.twitchmeme.dto.PostSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final YoutubeRepository youtubeRepository;

    public Long save(PostSaveRequestDto postSaveRequestDto) {
        Member member = memberRepository.findById(postSaveRequestDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + postSaveRequestDto.getMemberId()));
        Long postId = postRepository.save(postSaveRequestDto.toEntity(member)).getId();

        // 유튜브 링크가 있다면 저장
        if (postSaveRequestDto.getReference().size() > 0) {
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다. id=" + postId));
            ArrayList<String> list = postSaveRequestDto.getReference();

            //양방향 연관관계
            //Youtube 엔티티
            for (int i = 0; i < list.size(); i++) {
                youtubeRepository.save(postSaveRequestDto.toEntity(post, list.get(i)));
            }
            //Post 엔티티
            List<Youtube> youtubes = youtubeRepository.findByPost(post);
            post.setYoutubes(youtubes);
            postRepository.save(post);
        }
        return postId;
    }

    public List<PostResponseDto> findByBroadcastId(String broadcastId) {
        return postRepository.findByBroadcastId(broadcastId).stream().map(PostResponseDto::new).collect(Collectors.toList());
    }
}
