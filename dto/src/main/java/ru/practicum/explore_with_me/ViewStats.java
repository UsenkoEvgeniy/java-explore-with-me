package ru.practicum.explore_with_me;

public class ViewStats {
    private String app;
    private String uri;
    private Integer hits;

    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = Math.toIntExact(hits);
    }

    public String getApp() {
        return app;
    }

    public String getUri() {
        return uri;
    }

    public Integer getHits() {
        return hits;
    }
}
