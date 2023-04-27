package com.ewok.twitchmeme.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBroadcastId(String broadcastId);

    List<Post> findByMemberId(Long memberId);
}
