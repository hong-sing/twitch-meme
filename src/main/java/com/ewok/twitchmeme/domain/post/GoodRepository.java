package com.ewok.twitchmeme.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodRepository extends JpaRepository<Good, Long> {
    Good findByPostIdAndMemberId(Long postId, Long memberId);

    List<Good> findByMemberId(Long memberId);
}
