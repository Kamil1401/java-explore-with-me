package ru.practicum.rating;

import ru.practicum.rating.dto.RatingStats;

import java.util.List;
import java.util.Map;

public interface EventRatingService {

    void addRating(Long userId, Long eventId, RatingType ratingType);

    Map<Long, RatingStats> getRatingsForEvents(List<Long> eventIds);

    RatingStats getRatingForEvent(Long eventId);

    void deleteRating(Long userId, Long eventId);
}
