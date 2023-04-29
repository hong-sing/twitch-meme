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
        Reply parentReply = null;
        Reply savedReply = null;
        Long replyId = 0l;

        if (requestDto.getParentId() != null) { //대댓글인경우
            parentReply = replyRepository.findById(requestDto.getParentId()).get();

            //부모 댓글의 replies 필드에 대댓글 추가
            parentReply.addReply(requestDto.toEntity(post, member, parentReply));

        } else {    //댓글인경우
            savedReply = replyRepository.save(requestDto.toEntity(post, member));
        }

        //Member엔티티에 replyList 필드에 Reply 추가
        member.addReply(savedReply);

        return requestDto.getPostId();
    }

    /** 댓글 삭제 */
    public Long updateRemoveY(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (!isNoChild(replyId)) {
            reply.update('Y');
            return replyId;
        }

        if (isNoParent(reply)) {
            replyRepository.delete(reply);
            return replyId;
        }

        if (!isParentRemoveY(reply)) {
            replyRepository.delete(reply);
            return replyId;
        }

        if (isChildCountGreaterThanZero(reply)) {
            replyRepository.delete(reply);
            return replyId;
        }

        replyRepository.delete(reply.getParent());
        return replyId;
    }

    public List<ReplyResponseDto> findByPostId(Long postId) {
        return replyRepository.findByPostId(postId).stream().map(ReplyResponseDto::new).collect(Collectors.toList());
    }

    public Long updateContent(Long replyId, String content) {
        Reply reply = findById(replyId);
        reply.update(content);
        return replyId;
    }

    private boolean isChildCountGreaterThanZero(Reply reply) {
        return reply.getReplies().size() > 0;
    }

    private boolean isNoParent(Reply reply) {
        return reply.getParent()==null;
    }

    private boolean isParentRemoveY(Reply reply) {
        return reply.getParent().getRemove().equals('Y');
    }

    private boolean isNoChild(Long replyId) {
        Reply reply = findById(replyId);
        return reply.getReplies().size() == 0;
    }

    private Reply findById(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
    }

    public List<ReplyResponseDto> findByMemberId(Long memberId) {
        return replyRepository.findByMemberId(memberId).stream().map(ReplyResponseDto::new).collect(Collectors.toList());
    }
}
