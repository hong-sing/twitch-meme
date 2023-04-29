package com.ewok.twitchmeme.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBroadcastId(String broadcastId);
    Page<Post> findByBroadcastId(String broadcastId, Pageable pageable);

    List<Post> findByMemberId(Long memberId);
    Page<Post> findByMemberId(Long memberId, Pageable pageable);

    @Query("select p from Post p where p.title like %:meme% or p.summary like %:meme% or p.content like %:meme%")
    List<Post> findByMeme(@Param("meme") String meme);

    @Query("select p from Post p where p.title like %:meme% or p.summary like %:meme% or p.content like %:meme%")
    Page<Post> findByMeme(@Param("meme") String meme, Pageable pageable);

}
