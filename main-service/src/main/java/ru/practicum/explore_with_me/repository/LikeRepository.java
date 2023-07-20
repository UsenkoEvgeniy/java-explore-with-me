package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.Like;
import ru.practicum.explore_with_me.model.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long deleteByUserAndEvent(User user, Event event);
}