package com.ewok.twitchmeme.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Getter
@RequiredArgsConstructor
public class ChannelData {

    private final String broadcaster_language;
    private final String broadcaster_login;
    private final String display_name;
    private final String game_id;
    private final String game_name;
    private final String id;
    private final boolean is_live;
    private final ArrayList tag_ids;
    private final ArrayList tags;
    private final String thumbnail_url;
    private final String title;
    private final String started_at;

    public ChannelData(LinkedHashMap map) {
        this.broadcaster_language = (String) map.get("broadcaster_language");
        this.broadcaster_login = (String) map.get("broadcaster_login");
        this.display_name = (String) map.get("display_name");
        this.game_id = (String) map.get("game_id");
        this.game_name = (String) map.get("game_name");
        this.id = (String) map.get("id");
        this.is_live = (boolean) map.get("is_live");
        this.tag_ids = (ArrayList) map.get("tag_ids");
        this.tags = (ArrayList) map.get("tags");
        this.thumbnail_url = (String) map.get("thumbnail_url");
        this.title = (String) map.get("title");
        this.started_at = (String) map.get("started_at");
    }
}
