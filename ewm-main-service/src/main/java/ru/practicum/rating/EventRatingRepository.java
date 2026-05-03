package ru.practicum.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRatingRepository extends JpaRepository<EventRating, Long> {

    Optional<EventRating> findByUserIdAndEventId(Long userId, Long eventId);


    @Query("""
            SELECT r.event.id, r.ratingType, COUNT(r)
            FROM EventRating r
            WHERE r.event.id IN :eventIds
            GROUP BY r.event.id, r.ratingType
            """)
    List<Object[]> countRatingsForEvents(@Param("eventIds") List<Long> eventIds);
}