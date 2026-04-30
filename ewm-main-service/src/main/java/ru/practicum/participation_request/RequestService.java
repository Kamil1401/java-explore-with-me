package ru.practicum.participation_request;

import ru.practicum.participation_request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequestsByInitiator(Long userId, Long eventId);

    ParticipationRequest getRequestById(Long requestId);

    List<ParticipationRequest> getRequestsByIdInAndEventId(List<Long> ids, Long eventId);

    List<ParticipationRequest> getByEventIdAndStatus(Long eventId, Status status);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllUserRequests(Long userId);
}
