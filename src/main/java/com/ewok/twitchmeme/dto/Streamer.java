package com.ewok.twitchmeme.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;


@RequiredArgsConstructor
@Getter
public class Streamer implements Serializable {

    private String id;
    private String login;
    private String display_name;
    private String type;
    private String broadcaster_type;
    private String description;
    private String profile_image_url;
    private String offline_image_url;
    private int view_count;
    private String created_at;

    public Streamer(LinkedHashMap info) {
        this.id = (String) info.get("id");
        this.login = (String) info.get("login");
        this.display_name = (String) info.get("display_name");
        this.type = (String) info.get("type");
        this.broadcaster_type = (String) info.get("broadcaster_type");
        this.description = (String) info.get("description");
        this.profile_image_url = (String) info.get("profile_image_url");
        this.offline_image_url = (String) info.get("offline_image_url");
        this.view_count = (int) info.get("view_count");
        this.created_at = (String) info.get("created_at");
    }

}
