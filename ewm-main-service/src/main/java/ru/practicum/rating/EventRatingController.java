package ru.practicum.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventRatingController {
    private final EventRatingService ratingService;


    @PostMapping("/{userId}/events/{eventId}/rating")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRating(@PathVariable Long userId,
                          @PathVariable Long eventId,
                          @RequestParam RatingType ratingType) {

        ratingService.addRating(userId, eventId, ratingType);
    }


    @DeleteMapping("/{userId}/events/{eventId}/rating")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable Long userId,
                             @PathVariable Long eventId) {

        ratingService.deleteRating(userId, eventId);
    }
}
