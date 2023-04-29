package com.ewok.twitchmeme.dto.twitch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;


@RequiredArgsConstructor
@Getter
public class Follow implements Serializable {

    private String id;
    private String user_id;
    private String user_login;
    private String user_name;
    private String game_id;
    private String game_name;
    private String type;
    private String title;
    private Integer viewer_count;
    private String started_at;
    private String language;
    private String thumbnail_url;
    private ArrayList tag_ids;
    private ArrayList tags;
    private boolean is_mature;

    public Follow(LinkedHashMap info) {
        this.id = (String) info.get("id");
        this.user_id = (String) info.get("user_id");
        this.user_login = (String) info.get("user_login");
        this.user_name = (String) info.get("user_name");
        this.game_id = (String) info.get("game_id");
        this.game_name = (String) info.get("game_name");
        this.type = (String) info.get("type");
        this.title = (String) info.get("title");
        this.viewer_count = (Integer) info.get("viewer_count");
        this.started_at = (String) info.get("started_at");
        this.language = (String) info.get("language");
        this.thumbnail_url = (String) info.get("thumbnail_url");
        this.tag_ids = (ArrayList) info.get("tag_ids");
        this.tags = (ArrayList) info.get("tags");
        this.is_mature = (boolean) info.get("is_mature");
    }
}
