package ru.practicum.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

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
    public long countRating(Long eventId, RatingType ratingType) {
        return ratingRepository.countByEventIdAndRatingType(eventId, ratingType);
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
