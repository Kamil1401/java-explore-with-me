package ru.practicum.rating;

public interface EventRatingService {

    void addRating(Long userId, Long eventId, RatingType ratingType);

    long countRating(Long eventId, RatingType ratingType);

    void deleteRating(Long userId, Long eventId);
}
