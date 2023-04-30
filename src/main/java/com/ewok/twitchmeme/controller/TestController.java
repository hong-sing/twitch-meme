package com.ewok.twitchmeme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final HttpServletRequest request;

    @GetMapping("/baseurl")
    public String url() {
        return String.format("%s://%s:%d%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath());
    }
}
