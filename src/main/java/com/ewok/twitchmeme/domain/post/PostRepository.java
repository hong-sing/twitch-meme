package com.ewok.twitchmeme.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBroadcastId(String broadcastId);

    List<Post> findByMemberId(Long memberId);

    @Query("select p from Post p where p.title like %:meme% or p.summary like %:meme% or p.content like %:meme%")
    List<Post> findByMeme(@Param("meme") String meme);

}
