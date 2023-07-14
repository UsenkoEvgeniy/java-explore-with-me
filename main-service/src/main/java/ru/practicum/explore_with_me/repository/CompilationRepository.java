package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c from Compilation c where (:pinned is null or c.pinned = :pinned)")
    List<Compilation> findByPinned(Boolean pinned, Pageable pageable);

}
