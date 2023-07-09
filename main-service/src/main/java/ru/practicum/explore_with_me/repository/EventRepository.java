package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.State;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Optional<Event> findByIdAndState(Long id, State state);

    Optional<Event> findByIdAndInitiator_Id(Long id, Long id1);

    List<Event> findByInitiator_Id(Long id, Pageable pageable);
}
