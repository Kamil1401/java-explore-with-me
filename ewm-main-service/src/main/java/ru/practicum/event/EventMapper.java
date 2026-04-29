package ru.practicum.event;

import ru.practicum.category.CategoryMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.private_api.NewEventDto;
import ru.practicum.user.UserMapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class EventMapper {


    private static <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static Event toEntity(NewEventDto dto) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(dto.getLocation());
        event.setPaid(defaultIfNull(dto.getPaid(), false));
        event.setParticipantLimit(defaultIfNull(dto.getParticipantLimit(), 0));
        event.setRequestModeration(defaultIfNull(dto.getRequestModeration(), true));
        event.setTitle(dto.getTitle());

        return event;
    }

    public static EventFullDto toDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getPaid(),
                event.getEventDate(),
                UserMapper.toShortDto(event.getInitiator()),
                event.getViews(),
                event.getConfirmedRequests(),
                event.getDescription(),
                event.getParticipantLimit(),
                event.getState(),
                event.getCreatedOn(),
                event.getPublishedOn(),
                event.getLocation(),
                event.getRequestModeration()
        );
    }

    public static EventShortDto toShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.toShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static Set<EventShortDto> toEventShortDtos(Collection<Event> events) {
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toSet());
    }
}