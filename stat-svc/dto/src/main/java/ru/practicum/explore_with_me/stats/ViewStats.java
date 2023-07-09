package ru.practicum.explore_with_me.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Integer hits;

    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = Math.toIntExact(hits);
    }
}
