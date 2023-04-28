package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.post.Reply;
import com.ewok.twitchmeme.domain.post.ReplyRepository;
import com.ewok.twitchmeme.dto.ReplyRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReplyServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ReplyService replyService;

    private Post post;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .picture("이미지")
                .role(Role.USER)
                .nickname("닉네임")
                .twitchId(999L)
                .build();

        post = Post.builder()
                .title("제목")
                .summary("요약")
                .content("내용")
                .broadcastId("aaa123")
                .member(member)
                .build();

        postRepository.save(post);
        memberRepository.save(member);
    }

    @Test
    void 댓글_등록() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        ReplyRequestDto requestDto = ReplyRequestDto.builder()
                .comment("댓글")
                .postId(list.get(0).getId())
                .memberId(member.getId())
                .build();

        //when
        replyService.save(requestDto);

        //then
        List<Reply> replies = replyRepository.findByPostId(list.get(0).getId());
        assertThat(replies.get(0).getContent()).isEqualTo("댓글");
    }

    @Test
    void 대댓글_등록() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        List<Reply> replies = new ArrayList<>();
        Reply reply1 = Reply.builder()
                .post(list.get(0))
                .member(member)
                .remove('N')
                .content("댓글내용")
                .replies(replies)
                .build();
        Reply parentReply = replyRepository.save(reply1);

        ReplyRequestDto requestDto = ReplyRequestDto.builder()
                .comment("대댓글내용")
                .postId(list.get(0).getId())
                .memberId(member.getId())
                .parentId(parentReply.getId())
                .build();

        //when
        replyService.save(requestDto);

        //then
        assertThat(replyRepository.findById(parentReply.getId()).get().getReplies().get(0).getContent()).isEqualTo("대댓글내용");
    }

    @Test
    void 댓글_삭제_자식댓글_없음() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        List<Reply> replies = new ArrayList<>();
        Reply reply = Reply.builder()
                .post(list.get(0))
                .member(member)
                .replies(replies)
                .content("댓글내용")
                .build();
        Long saveId = replyRepository.save(reply).getId();

        //when
        replyService.updateRemoveY(saveId);

        //then
        assertThat(replyRepository.findById(saveId)).isEmpty();
    }

    @Test
    void 댓글_삭제_자식댓글_있음() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        List<Reply> replies = new ArrayList<>();
        Reply reply1 = Reply.builder()
                .post(list.get(0))
                .member(member)
                .remove('N')
                .content("댓글내용")
                .replies(replies)
                .build();
        Reply parentReply = replyRepository.save(reply1);

        List<Reply> nullList = new ArrayList<>();
        Reply reply2 = Reply.builder()
                .post(list.get(0))
                .member(member)
                .content("대댓글내용")
                .remove('N')
                .parent(parentReply)
                .replies(nullList)
                .build();

        Reply childReply = replyRepository.save(reply2);
        parentReply.getReplies().add(childReply);

        //when
        replyService.updateRemoveY(parentReply.getId());

        //then
        assertThat(replyRepository.findById(parentReply.getId()).get().getRemove()).isEqualTo('Y');
        assertThat(replyRepository.findById(childReply.getId()).get().getRemove()).isEqualTo('N');
    }


    @Test
    void 대댓글_삭제() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        Reply reply1 = Reply.builder()
                .post(list.get(0))
                .member(member)
                .remove('N')
                .content("댓글내용")
                .build();
        Reply parentReply = replyRepository.save(reply1);
        List<Reply> replies = new ArrayList<>();
        replies.add(parentReply);

        Reply reply2 = Reply.builder()
                .post(list.get(0))
                .member(member)
                .remove('N')
                .content("대댓글내용")
                .parent(parentReply)
                .replies(replies)
                .build();
        Long id = replyRepository.save(reply2).getId();

        //when
        replyService.updateRemoveY(id);

        //then
        assertThat(replyRepository.findById(id).get().getRemove()).isEqualTo('Y');
        assertThat(replyRepository.findById(reply1.getId()).get().getRemove()).isEqualTo('N');
    }

    @Test
    void 댓글_수정() {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Member member = memberRepository.findByTwitchId(999L).get();
        Reply reply = Reply.builder()
                .post(list.get(0))
                .member(member)
                .remove('N')
                .content("댓글내용")
                .build();
        Reply parentReply = replyRepository.save(reply);

        //when
        replyService.updateContent(parentReply.getId(), "수정된 댓글 내용");

        //then
        assertThat(replyRepository.findById(parentReply.getId()).get().getContent()).isEqualTo("수정된 댓글 내용");
    }
}