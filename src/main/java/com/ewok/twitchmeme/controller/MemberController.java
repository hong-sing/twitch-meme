package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.member.MemberResponseDto;
import com.ewok.twitchmeme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final HttpSession httpSession;

    @DeleteMapping("/api/v1/member/{memberId}")
    public Long delete(@PathVariable Long memberId) {
        SecurityContextHolder.clearContext();
        httpSession.invalidate();
        return memberService.delete(memberId);
    }

    @GetMapping("/api/v1/member")
    public List<MemberResponseDto> list() {
        return memberService.findAll();
    }
}
