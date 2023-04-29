package com.ewok.twitchmeme.dto.twitch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Getter
@RequiredArgsConstructor
public class StreamInfo {

    private final ArrayList data;
    private final LinkedHashMap pagination;

    public StreamInfo(LinkedHashMap map) {
        this.data = (ArrayList) map.get("data");
        this.pagination = (LinkedHashMap) map.get("pagination");
    }
}
