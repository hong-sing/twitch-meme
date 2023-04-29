package com.ewok.twitchmeme.dto.twitch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Getter
@RequiredArgsConstructor
public class StreamInfoData {

    private final String id;
    private final String user_id;
    private final String user_login;
    private final String user_name;
    private final String game_id;
    private final String game_name;
    private final String type;
    private final String title;
    private final ArrayList tags;
    private final Integer viewer_count;
    private final String started_at;
    private final String language;
    private final String thumbnail_url;
    private final boolean is_mature;

    public StreamInfoData(LinkedHashMap map) {
        this.id = (String) map.get("id");
        this.user_id = (String) map.get("user_id");
        this.user_login = (String) map.get("user_login");
        this.user_name = (String) map.get("user_name");
        this.game_id = (String) map.get("game_id");
        this.game_name = (String) map.get("game_name");
        this.type = (String) map.get("type");
        this.title = (String) map.get("title");
        this.tags = (ArrayList) map.get("tags");
        this.viewer_count = (Integer) map.get("viewer_count");
        this.started_at = (String) map.get("started_at");
        this.language = (String) map.get("language");
        this.thumbnail_url = (String) map.get("thumbnail_url");
        this.is_mature = (boolean) map.get("is_mature");
    }
}
