package ru.practicum.participation_request;

import ru.practicum.participation_request.dto.ParticipationRequestDto;

public class RequestMapper {

    public static ParticipationRequestDto toDto(ParticipationRequest request) {

        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }
}
