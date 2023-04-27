package com.ewok.twitchmeme.dto.twitch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Getter
@RequiredArgsConstructor
public class FollowInfoData {

    private final String broadcaster_id;
    private final String broadcaster_login;
    private final String broadcaster_name;
    private final String followed_at;

    public FollowInfoData(LinkedHashMap map) {
//        String followed_date = (String) map.get("followed_at");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        LocalDateTime date = LocalDateTime.parse(followed_date, formatter);

        this.broadcaster_id = (String) map.get("broadcaster_id");
        this.broadcaster_login = (String) map.get("broadcaster_login");
        this.broadcaster_name = (String) map.get("broadcaster_name");
        this.followed_at = (String) map.get("followed_at");
    }
}
