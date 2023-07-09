package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.Request;
import ru.practicum.explore_with_me.model.State;

import java.util.Collection;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester_Id(Long id);

    List<Request> findByIdInAndEvent_IdAndEvent_Initiator_IdAndStatus(Collection<Long> ids, Long eventId, Long userId, State status);

    List<Request> findByEvent_IdAndEvent_Initiator_Id(Long eventId, Long userId);
}
