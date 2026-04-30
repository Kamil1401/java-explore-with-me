package ru.practicum.participation_request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.EventCapacityException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestCreationException;
import ru.practicum.participation_request.dto.ParticipationRequestDto;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventRepository eventRepository;


    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new RequestCreationException("Нельзя добавить повторный запрос");
        }

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new RequestCreationException("Нельзя запросить участие в собственном событии");
        }
        if (event.getState() == State.CANCELED || event.getState() == State.PENDING) {
            throw new RequestCreationException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new EventCapacityException("Достигнут лимит заявок");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }

        ParticipationRequest savedRequest = requestRepository.save(request);

        return RequestMapper.toDto(savedRequest);
    }


    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        userService.getUserById(userId);
        ParticipationRequest request = getRequestById(requestId);

        request.setStatus(Status.REJECTED);
        ParticipationRequest savedRequest = requestRepository.save(request);

        return RequestMapper.toDto(savedRequest);
    }


    @Override
    public List<ParticipationRequestDto> getEventRequestsByInitiator(Long userId, Long eventId) {
        userService.getUserById(userId);
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));

        List<ParticipationRequest> requests = requestRepository.findByInitiatorIdAndEventId(userId, eventId);

        return requests.stream()
                .map(RequestMapper::toDto)
                .toList();
    }


    @Override
    public List<ParticipationRequest> getRequestsByIdInAndEventId(List<Long> ids, Long eventId) {
        return requestRepository.findByIdInAndEventId(ids, eventId);
    }


    @Override
    public List<ParticipationRequest> getByEventIdAndStatus(Long eventId, Status status) {
        return requestRepository.findByEventIdAndStatus(eventId, Status.PENDING);
    }


    @Override
    public ParticipationRequest getRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID " + " не найден"));
    }


    @Override
    public List<ParticipationRequestDto> getAllUserRequests(Long userId) {
        userService.getUserById(userId);

        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .toList();
    }
}