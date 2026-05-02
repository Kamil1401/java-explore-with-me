package ru.practicum.participation_request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.participation_request.dto.ParticipationRequestDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
