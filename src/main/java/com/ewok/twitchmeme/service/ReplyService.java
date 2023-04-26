package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.post.Reply;
import com.ewok.twitchmeme.domain.post.ReplyRepository;
import com.ewok.twitchmeme.dto.ReplyRequestDto;
import com.ewok.twitchmeme.dto.ReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(ReplyRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        Reply reply = null;
        if (requestDto.getParentId() != null) {
            reply = replyRepository.findById(requestDto.getParentId()).orElse(null);
            reply.getReplies().add(requestDto.toEntity(post, member, reply));

        }
        Long replyId = replyRepository.save(requestDto.toEntity(post, member, reply)).getId();
        return replyId;
    }

    public List<ReplyResponseDto> findByPostId(Long postId) {
        return replyRepository.findByPostId(postId).stream().map(ReplyResponseDto::new).collect(Collectors.toList());
    }
}
