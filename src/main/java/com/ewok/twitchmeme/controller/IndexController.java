package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.ChannelData;
import com.ewok.twitchmeme.dto.LoginMember;
import com.ewok.twitchmeme.dto.SessionMember;
import com.ewok.twitchmeme.service.TwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final TwitchService twitchService;

    @GetMapping("/")
    public String index(Model model, @LoginMember SessionMember member) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        return "index";
    }

    @GetMapping("/meme/streamer/{streamer}")
    public String search(Model model, @LoginMember SessionMember member, @PathVariable String streamer) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("channels", twitchService.getChannelSearchResult(streamer));
        return "meme/channel-list";
    }

    @GetMapping("/meme/post/{broadcaster_login}")
    public String post(Model model, @PathVariable String broadcaster_login, @LoginMember SessionMember member) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("streamer", twitchService.getStreamerInfo(broadcaster_login));
        return "meme/post";
    }

    @GetMapping("/meme/post-save/{broadcastId}")
    public String postSave(Model model, @LoginMember SessionMember member, @PathVariable String broadcastId) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("broadcastId", broadcastId);
        return "meme/post-save";
    }
}
