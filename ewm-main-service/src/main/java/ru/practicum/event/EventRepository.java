package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query(value = """
            SELECT *
            FROM     events
            WHERE    initiator_id = :userId
            ORDER BY id
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Event> findUserEvents(@Param("userId") Long userId, @Param("from") int from, @Param("size") int size);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    boolean existsByCategoryId(Long categoryId);


    @Query(value = """
            SELECT *
            FROM     events
            WHERE    (:users IS NULL OR initiator_id IN (:users))
              AND    (:states IS NULL OR state IN (:states))
              AND    (:categories IS NULL OR category_id IN (:categories))
              AND    (:rangeStart IS NULL OR event_date >= :rangeStart)
              AND    (:rangeEnd IS NULL OR event_date <= :rangeEnd)
            ORDER BY id
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Event> findAll(@Param("users") List<Long> users,
                        @Param("states") List<String> states,
                        @Param("categories") List<Long> categories,
                        @Param("rangeStart") LocalDateTime rangeStart,
                        @Param("rangeEnd") LocalDateTime rangeEnd,
                        @Param("from") int from,
                        @Param("size") int size);
}