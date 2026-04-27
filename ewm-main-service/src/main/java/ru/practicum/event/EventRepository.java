package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

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

    @Query("""
    SELECT e
    FROM   Event e
    WHERE  e.state = 'PUBLISHED'
      AND  (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))
                          OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))
      AND  (:categories IS NULL OR e.category.id IN :categories)
      AND  (:paid IS NULL OR e.paid = :paid)
      AND  (:rangeStart IS NULL OR e.eventDate >= :rangeStart)
      AND  (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)
      AND  (:onlyAvailable IS NOT TRUE OR e.participantLimit = 0 OR e.confirmedRequests < e.participantLimit)
    """)
    List<Event> searchPublicEvents(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable
    );
}