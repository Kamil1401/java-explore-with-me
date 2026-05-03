package ru.practicum.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.dto.*;
import ru.practicum.event.dto.admin_api.UpdateEventAdminRequest;
import ru.practicum.event.dto.private_api.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.private_api.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.private_api.NewEventDto;
import ru.practicum.event.dto.private_api.UpdateEventUserRequest;
import ru.practicum.participation_request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(NewEventDto dto, Long userId);

    Event getEventById(Long eventId);

    List<EventFullDto> getUserEvents(Long userId, int from, int size);

    EventFullDto getUserEvent(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequestsByInitiator(Long userId, Long eventId);

    EventFullDto updateInitiatorEvent(UpdateEventUserRequest request, Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatuses(EventRequestStatusUpdateRequest request,
                                                         Long userId, Long eventId);

    EventFullDto updateAdminEvent(UpdateEventAdminRequest adminRequest, Long eventId);

    List<EventFullDto> getEventsByParameters(List<Long> users, List<String> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             int from, int size);

    List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                        int from, int size, HttpServletRequest request);

    EventFullDto getPublicEventById(Long id, HttpServletRequest request);
}