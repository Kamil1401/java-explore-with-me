package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.dto.private_api.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.private_api.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.private_api.NewEventDto;
import ru.practicum.event.dto.private_api.UpdateEventUserRequest;
import ru.practicum.participation_request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;


    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> createEvent(@Valid @RequestBody NewEventDto dto, @PathVariable Long userId) {
        EventFullDto result = eventService.createEvent(dto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @GetMapping("/{userId}/events")
    public List<EventFullDto> getUserEvents(@PathVariable(required = false) Long userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {

        return eventService.getUserEvents(userId, from, size);
    }


    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateInitiatorEvent(@Valid @RequestBody UpdateEventUserRequest updateEventUserRequest,
                                             @PathVariable Long userId,
                                             @PathVariable Long eventId) {

        return eventService.updateInitiatorEvent(updateEventUserRequest, userId, eventId);
    }


    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId,
                                     @PathVariable Long eventId) {

        return eventService.getUserEvent(userId, eventId);
    }


    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatuses(@Valid @RequestBody EventRequestStatusUpdateRequest request,
                                                                @PathVariable Long userId,
                                                                @PathVariable Long eventId) {

        return eventService.updateRequestStatuses(request, userId, eventId);
    }


    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsForCurrentUser(@PathVariable Long userId,
                                                                                @PathVariable Long eventId) {

        return eventService.getEventRequestsByInitiator(userId, eventId);
    }
}