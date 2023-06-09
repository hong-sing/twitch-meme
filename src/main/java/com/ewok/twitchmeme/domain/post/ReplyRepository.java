package com.ewok.twitchmeme.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByPostId(Long postId);

    List<Reply> findByMemberId(Long memberId);
}
