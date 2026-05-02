package ru.practicum.rating.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.rating.RatingType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRatingDto {

    @NotNull
    private RatingType ratingType;
}