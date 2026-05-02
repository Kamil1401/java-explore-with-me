package ru.practicum.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRatingRepository extends JpaRepository<EventRating, Long> {

    Optional<EventRating> findByUserIdAndEventId(Long userId, Long eventId);

    long countByEventIdAndRatingType(Long eventId, RatingType ratingType);
}