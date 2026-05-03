package ru.practicum.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.rating.dto.RatingStats;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventRatingServiceImpl implements EventRatingService {
    private final EventRatingRepository ratingRepository;
    private final UserService userService;
    private final EventRepository eventRepository;


    @Transactional
    @Override
    public void addRating(Long userId, Long eventId, RatingType ratingType) {
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID " + eventId + " не найдено"));

        Optional<EventRating> existing = ratingRepository.findByUserIdAndEventId(userId, eventId);

        if (existing.isPresent()) {
            EventRating rating = existing.get();

            if (rating.getRatingType() == ratingType) {
                return;
            }
            rating.setRatingType(ratingType);

        } else {
            EventRating rating = new EventRating();
            rating.setUser(user);
            rating.setEvent(event);
            rating.setRatingType(ratingType);

            ratingRepository.save(rating);
        }
    }


    @Override
    public Map<Long, RatingStats> getRatingsForEvents(List<Long> eventIds) {

        List<Object[]> rows = ratingRepository.countRatingsForEvents(eventIds);

        Map<Long, RatingStats> eventRatings = new HashMap<>();

        for (Object[] row : rows) {
            Long eventId = (Long) row[0];
            RatingType type = (RatingType) row[1];
            Long count = (Long) row[2];

            eventRatings.putIfAbsent(eventId, new RatingStats(eventId));
            RatingStats stats = eventRatings.get(eventId);

            if (type == RatingType.LIKE) {
                stats.addLikes(count);
            } else {
                stats.addDislikes(count);
            }
        }

        return eventRatings;
    }


    @Override
    public RatingStats getRatingForEvent(Long eventId) {
        Map<Long, RatingStats> map = getRatingsForEvents(List.of(eventId));

        return map.getOrDefault(eventId, new RatingStats(eventId));
    }


    @Transactional
    @Override
    public void deleteRating(Long userId, Long eventId) {
        userService.getUserById(userId);
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID " + eventId + " не найдено"));

        Optional<EventRating> ratingOpt =
                ratingRepository.findByUserIdAndEventId(userId, eventId);

        if (ratingOpt.isEmpty()) {
            throw new NotFoundException("Рейтинг не найден");
        }

        ratingRepository.delete(ratingOpt.get());
    }
}
