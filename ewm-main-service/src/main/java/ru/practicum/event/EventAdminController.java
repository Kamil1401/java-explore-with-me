package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.admin_api.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;


    @PatchMapping("/{eventId}")
    public EventFullDto updateAdminEvent(@Valid @RequestBody UpdateEventAdminRequest adminRequest,
                                         @PathVariable Long eventId) {

        return eventService.updateAdminEvent(adminRequest, eventId);
    }


    @GetMapping
    public List<EventFullDto> getEventsByParameters(@RequestParam(required = false) List<Long> users,
                                                    @RequestParam(required = false) List<String> states,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) LocalDateTime rangeStart,
                                                    @RequestParam(required = false) LocalDateTime rangeEnd,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {

        return eventService.getEventsByParameters(users,states, categories, rangeStart, rangeEnd, from, size);
    }

}
