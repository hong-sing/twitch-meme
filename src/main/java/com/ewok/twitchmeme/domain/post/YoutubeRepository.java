package com.ewok.twitchmeme.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YoutubeRepository extends JpaRepository<Youtube, Long> {
    List<Youtube> findByPost(Post post);
}
