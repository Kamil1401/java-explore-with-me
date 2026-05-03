package ru.practicum.rating.dto;

import lombok.Getter;

@Getter
public class RatingStats {
    private Long eventId;
    private long likes;
    private long dislikes;

    public RatingStats(Long eventId) {
        this.eventId = eventId;
    }


    public void addLikes(long count) {
        this.likes = count;
    }

    public void addDislikes(long count) {
        this.dislikes = count;
    }
}