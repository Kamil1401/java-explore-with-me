package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.enums.State;
import ru.practicum.event.Location;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Boolean paid;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Long views;
    private Integer confirmedRequest;
    private String description;
    private Integer participantLimit;
    private State state;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private Location location;
    private Boolean requestModeration;
}