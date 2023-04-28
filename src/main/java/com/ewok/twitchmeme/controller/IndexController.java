package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.dto.LoginMember;
import com.ewok.twitchmeme.dto.SessionMember;
import com.ewok.twitchmeme.dto.post.PostPagingListResponseDto;
import com.ewok.twitchmeme.service.PostService;
import com.ewok.twitchmeme.service.ReplyService;
import com.ewok.twitchmeme.service.TwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final TwitchService twitchService;
    private final PostService postService;
    private final ReplyService replyService;

    @GetMapping("/")
    public String index(Model model, @LoginMember SessionMember member) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("streamInfoData", twitchService.getStreamInfo("ko"));
        return "index";
    }

    @GetMapping("/meme/channel-list/{streamer}")
    public String searchChannel(Model model, @LoginMember SessionMember member, @PathVariable String streamer) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("channels", twitchService.getChannelSearchResult(streamer));
        return "meme/channel-list";
    }

    @GetMapping("/meme/post/{broadcaster_login}")
    public String post(Model model, @PathVariable String broadcaster_login, @LoginMember SessionMember member, @PageableDefault(sort = "modifiedDate", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("streamer", twitchService.getStreamerInfo(broadcaster_login));
        model.addAttribute("posts", postService.pagingFindByBroadcastId(broadcaster_login, pageable));
        model.addAttribute("pageNo", pageNo);
        return "meme/post-streamer";
    }

    @GetMapping("/meme/post-save/{broadcastId}")
    public String postSave(Model model, @LoginMember SessionMember member, @PathVariable String broadcastId) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("broadcastId", broadcastId);
        return "meme/post-save";
    }

    @GetMapping("/meme/post-detail/{postId}")
    public String postDetail(Model model, @LoginMember SessionMember member, @PathVariable Long postId) {
        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("post", postService.findById(postId, member.getId()));
            model.addAttribute("reply", replyService.findByPostId(postId));
        } else {
            model.addAttribute("post", postService.findById(postId, null));
            model.addAttribute("reply", replyService.findByPostId(postId));
        }
        return "meme/post-detail";
    }

    @GetMapping("/meme/post-update/{postId}")
    public String postUpdate(Model model, @LoginMember SessionMember member, @PathVariable Long postId) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("post", postService.findById(postId, member.getId()));
        return "meme/post-update";
    }

    @GetMapping("/meme/post-meme/{meme}")
    public String searchMeme(Model model, @LoginMember SessionMember member, @PathVariable String meme, Pageable pageable, @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
        System.out.println(meme);
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("posts", postService.findByMeme(meme, pageable));
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("meme", meme);
        return "meme/post-meme";
    }

    @GetMapping("/streamer/channel-search")
    public String searchStreamer(Model model, @LoginMember SessionMember member) {
        model.addAttribute("memeber", member);
        return "streamer/channel-search";
    }

    @GetMapping("/streamer/channel-list/{streamer}")
    public String searchStreamerList(Model model, @PathVariable String streamer, @LoginMember SessionMember member) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("channels", twitchService.getChannelSearchResult(streamer));
        return "streamer/channel-list";
    }

    @GetMapping("/streamer/streamer-follow/{streamerId}")
    public String getFollowList(Model model, @LoginMember SessionMember member, @PathVariable String streamerId) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("followList", twitchService.getStreamerFollowList(streamerId));
        return "streamer/streamer-follow";
    }
}
