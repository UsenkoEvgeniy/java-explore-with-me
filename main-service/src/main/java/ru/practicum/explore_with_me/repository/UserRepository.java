package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.user.UserRatingDto;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where (coalesce(:ids) is null or u.id in :ids)")
    List<User> findByIds(@Param("ids") Collection<Long> ids, Pageable pageable);

    @Query(value = "select u.id, u.name, u.email, coalesce(sum(l.int_like), 0) as rating from users u " +
            "left join events e on u.id = e.initiator left join event_likes l on e.id = l.event_id " +
            "group by (u.id, u.name, u.email) order by rating desc", nativeQuery = true)
    List<UserRatingDto> findUsersByRating(Pageable pageable);

    @Query(value = "select u.id, u.name, u.email, coalesce(sum(l.int_like), 0) as rating from users u " +
            "left join events e on u.id = e.initiator left join event_likes l on e.id = l.event_id " +
            "where u.id = :uid " +
            "group by (u.id, u.name, u.email)", nativeQuery = true)
    UserRatingDto findUserWithRating(@Param("uid") Long userId);

}
