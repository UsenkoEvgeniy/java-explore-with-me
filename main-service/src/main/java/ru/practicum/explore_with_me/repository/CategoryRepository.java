package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
