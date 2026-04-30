package ru.practicum.participation_request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation_request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestService requestService;


    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable Long userId,
                                                                 @RequestParam Long eventId) {

        ParticipationRequestDto result = requestService.createRequest(userId, eventId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId,
                                                              @PathVariable Long requestId) {

        return requestService.cancelParticipationRequest(userId, requestId);
    }


    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllUserRequests(@PathVariable Long userId) {
        return requestService.getAllUserRequests(userId);
    }
}
