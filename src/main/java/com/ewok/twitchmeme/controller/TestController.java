package com.ewok.twitchmeme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {


    @Value("${spring.security.oauth2.client.registration.twitch.redirect-uri}")
    private String url;

    @GetMapping("/baseurl")
    public String url() {
        return url;
    }
}
