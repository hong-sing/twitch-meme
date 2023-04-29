package com.ewok.twitchmeme.service;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.dto.member.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long delete(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        memberRepository.delete(member);
        return memberId;
    }


    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll().stream().map(MemberResponseDto::new).collect(Collectors.toList());
    }
}
