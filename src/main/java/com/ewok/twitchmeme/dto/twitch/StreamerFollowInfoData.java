package com.ewok.twitchmeme.dto.twitch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;

@Getter
@RequiredArgsConstructor
public class StreamerFollowInfoData {

    private final String from_id;
    private final String from_login;
    private final String from_name;
    private final String to_id;
    private final String to_login;
    private final String to_name;
    private final String followed_at;

    public StreamerFollowInfoData(LinkedHashMap map) {
        this.from_id = (String) map.get("from_id");
        this.from_login = (String) map.get("from_login");
        this.from_name = (String) map.get("from_name");
        this.to_id = (String) map.get("to_id");
        this.to_login = (String) map.get("to_login");
        this.to_name = (String) map.get("to_name");
        this.followed_at = (String) map.get("followed_at");
    }
}
