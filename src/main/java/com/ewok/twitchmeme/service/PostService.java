package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.post.*;
import com.ewok.twitchmeme.dto.PostResponseDto;
import com.ewok.twitchmeme.dto.PostSaveRequestDto;
import com.ewok.twitchmeme.dto.PostUpdateRequestDto;
import com.ewok.twitchmeme.dto.PostsDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final YoutubeRepository youtubeRepository;

    /** 게시글 저장 */
    @Transactional
    public Long save(PostSaveRequestDto postSaveRequestDto) {
        Member member = memberRepository.findById(postSaveRequestDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + postSaveRequestDto.getMemberId()));
        Post post = new Post(postSaveRequestDto.getTitle(), postSaveRequestDto.getSummary(), postSaveRequestDto.getBroadcastId(), postSaveRequestDto.getContent(), member);

        // 유튜브 링크가 있다면 저장
        if (postSaveRequestDto.getReference().size() > 0) {
            ArrayList<String> list = postSaveRequestDto.getReference();

            //Post 엔티티의 유튜브 컬렉션에 유튜브 엔티티 추가
            for (int i = 0; i < list.size(); i++) {
                Youtube saveYoutube = youtubeRepository.save(postSaveRequestDto.toEntity(post, list.get(i)));
                post.addYoutube(saveYoutube);
            }
        }

        Post savedPost = postRepository.save(post);

        //Member 엔티티의 postList 필드에 Post 추가
        member.addPost(savedPost);

        return savedPost.getId();
    }

    /** 게시글 수정 */
    @Transactional
    public Long update(Long postId, PostUpdateRequestDto updateRequestDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postId));
        List<Youtube> youtubes = youtubeRepository.findByPost(findPost);
        for (Youtube youtube : youtubes) {
            youtubeRepository.delete(youtube);
        }

        if (updateRequestDto.getReference().size() > 0) {
            ArrayList<String> list = updateRequestDto.getReference();
            for (int i = 0; i < list.size(); i++) {
                youtubeRepository.save(updateRequestDto.toEntity(findPost, list.get(i)));
            }
            youtubes = youtubeRepository.findByPost(findPost);
        } else {
            youtubes = null;
        }
        findPost.update(updateRequestDto.getTitle(), updateRequestDto.getSummary(), updateRequestDto.getContent(), youtubes);
        return postId;
    }

    /** 게시글 삭제 */
    @Transactional
    public Long delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postId));
        postRepository.delete(post);
        return postId;
    }

    public List<PostResponseDto> findByBroadcastId(String broadcastId) {
        return postRepository.findByBroadcastId(broadcastId).stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    public PostsDetailResponseDto findById(Long postId, Long memberId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postId));
        List<Good> goods = post.getGoods();
        boolean checkGood = false;
        if (memberId != null) {
            for (int i = 0; i < goods.size(); i++) {
                if (goods.get(i).getPost().getMember().getId().equals(memberId)) {
                    checkGood = true;
                }
            }
        }
        return new PostsDetailResponseDto(post, checkGood);
    }

    public List<PostResponseDto> findByMemberId(Long memberId) {
        return postRepository.findByMemberId(memberId).stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    public List<PostResponseDto> findByMeme(String meme) {
        return postRepository.findByMeme(meme).stream().map(PostResponseDto::new).collect(Collectors.toList());
    }
}
