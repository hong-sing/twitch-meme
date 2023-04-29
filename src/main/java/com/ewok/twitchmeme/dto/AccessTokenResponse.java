package com.ewok.twitchmeme.dto;

import lombok.Getter;

@Getter
public class AccessTokenResponse {

    private String access_token;
    private String token_type;
    private int expires_in;

}
