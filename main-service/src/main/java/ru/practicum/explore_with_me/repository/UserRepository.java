package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where (coalesce(:ids) is null or u.id in :ids)")
    List<User> findByIds(@Param("ids") Collection<Long> ids, Pageable pageable);

}
