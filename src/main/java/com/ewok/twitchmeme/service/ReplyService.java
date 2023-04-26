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
@Transactional
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /** 댓글 등록 */
    public Long save(ReplyRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        Reply reply = null;
        Long replyId = 0l;

        if (requestDto.getParentId() != null) { //대댓글인경우
            reply = replyRepository.findById(requestDto.getParentId()).get();
            Reply saveReply = replyRepository.save(requestDto.toEntity(post, member, reply));

            //부모 댓글에 대댓글 컬렉션 추가
            reply.getReplies().add(saveReply);
            replyId = saveReply.getId();

        } else {    //댓글인경우
            replyId = replyRepository.save(requestDto.toEntity(post, member)).getId();
        }

        return replyId;
    }

    /** 댓글 삭제 */
    public Long updateRemoveY(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        reply.update('Y');

        if (isNoChild(replyId)) {
            delete(replyId);
        }

        return replyId;
    }


    public List<ReplyResponseDto> findByPostId(Long postId) {
        return replyRepository.findByPostId(postId).stream().map(ReplyResponseDto::new).collect(Collectors.toList());
    }

    private boolean isNoChild(Long replyId) {
        Reply reply = findById(replyId);
        return reply.getReplies() == null;
    }

    private void delete(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        replyRepository.delete(reply);
    }

    private Reply findById(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
    }

}
