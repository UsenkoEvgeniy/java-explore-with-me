package ru.practicum.explore_with_me;

import lombok.Getter;

@Getter
public class ViewStats {
    private final String app;
    private final String uri;
    private final Integer hits;

    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = Math.toIntExact(hits);
    }
}
