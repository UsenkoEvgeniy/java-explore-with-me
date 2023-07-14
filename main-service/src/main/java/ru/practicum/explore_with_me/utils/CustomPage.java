package ru.practicum.explore_with_me.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CustomPage extends PageRequest {
    public CustomPage(int from, int size) {
        super(from / size, size, Sort.unsorted());
    }
}
