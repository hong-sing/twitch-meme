package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.LoginMember;
import com.ewok.twitchmeme.dto.SessionMember;
import com.ewok.twitchmeme.service.PostService;
import com.ewok.twitchmeme.service.TwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MypageController {

    private final TwitchService twitchService;
    private final PostService postService;

    @GetMapping("/mypage")
    public String mypage(Model model, @LoginMember SessionMember member) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        return "mypage/mypage";
    }

    @GetMapping("/mypage/follow")
    public String follow(Model model, @LoginMember SessionMember member) {
        model.addAttribute("member", member);
        model.addAttribute("follow", twitchService.getFollowList(member.getId()));
        return "mypage/follow";
    }

    @GetMapping("/mypage/writed-meme")
    public String writedMeme(Model model, @LoginMember SessionMember member) {
        model.addAttribute("member", member);
        model.addAttribute("posts", postService.findByMemberId(member.getId()));
        return "mypage/writed-meme";
    }

}