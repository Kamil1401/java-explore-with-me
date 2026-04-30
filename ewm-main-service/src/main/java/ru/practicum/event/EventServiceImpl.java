package ru.practicum.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStats;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.enums.State;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.admin_api.AdminStateAction;
import ru.practicum.event.dto.admin_api.UpdateEventAdminRequest;
import ru.practicum.event.dto.private_api.*;
import ru.practicum.exception.*;
import ru.practicum.participation_request.ParticipationRequest;
import ru.practicum.participation_request.RequestMapper;
import ru.practicum.participation_request.RequestService;
import ru.practicum.participation_request.Status;
import ru.practicum.participation_request.dto.ParticipationRequestDto;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final RequestService requestService;
    private final StatsClient statsClient;


//    P R I V A T E _ A P I

    @Override
    public EventFullDto createEvent(NewEventDto dto, Long userId) {
        User user = userService.getUserById(userId);
        Event event = EventMapper.toEntity(dto);

        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(null);
        event.setCategory(categoryService.getCategoryById(dto.getCategory()));
        event.setInitiator(user);
        event.setState(State.PENDING);
        event.setViews(0L);

        Event savedEvent = eventRepository.save(event);

        return EventMapper.toDto(savedEvent);
    }


    @Override
    public EventFullDto updateInitiatorEvent(UpdateEventUserRequest request, Long userId, Long eventId) {
        Event event = getEventById(eventId);
        userService.getUserById(userId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new InitiatorRequiredException("Пользователь не является создателем события");
        }

        if (event.getState() == State.PUBLISHED) {
            throw new EventModificationException("Событие опубликовано и не подлежит изменениям");
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            Category category = categoryService.getCategoryById(request.getCategory());
            event.setCategory(category);
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {

            if (request.getStateAction() == StateAction.SEND_TO_REVIEW) {
                event.setState(State.PENDING);

            } else if (request.getStateAction() == StateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            }
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        Event savedEvent = eventRepository.save(event);

        return EventMapper.toDto(savedEvent);
    }


    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatuses(EventRequestStatusUpdateRequest updateRequest,
                                                                Long userId,
                                                                Long eventId) {

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getParticipantLimit() == 0 || event.getRequestModeration() == Boolean.FALSE) {
            List<ParticipationRequest> requests =
                    requestService.getRequestsByIdInAndEventId(updateRequest.getRequestIds(), eventId);

            List<ParticipationRequestDto> requestDtos = new ArrayList<>();

            for (ParticipationRequest request : requests) {
                if (request.getStatus() != Status.PENDING) {
                    throw new StatusChangeException("Запрос должен иметь статус \"PENDING\"");
                }
                request.setStatus(Status.CONFIRMED);
                requestDtos.add(RequestMapper.toDto(request));
            }

            return new EventRequestStatusUpdateResult(requestDtos, List.of());
        }

        List<ParticipationRequest> requests =
                requestService.getRequestsByIdInAndEventId(updateRequest.getRequestIds(), eventId);

        List<ParticipationRequest> confirmed = new ArrayList<>();
        List<ParticipationRequest> rejected = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (request.getStatus() != Status.PENDING) {
                throw new StatusChangeException("Запрос должен иметь статус \"PENDING\"");
            }
            if (updateRequest.getStatus() == Status.REJECTED) {
                request.setStatus(Status.REJECTED);
                rejected.add(request);
                continue;
            }
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new EventCapacityException("Достигнут лимит количества заявок");
            }
            request.setStatus(Status.CONFIRMED);
            confirmed.add(request);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);

            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                List<ParticipationRequest> pending = requestService.getByEventIdAndStatus(eventId, Status.PENDING);

                for (ParticipationRequest p : pending) {
                    p.setStatus(Status.REJECTED);
                    rejected.add(p);
                }
                break;
            }
        }

        return new EventRequestStatusUpdateResult(
                confirmed.stream()
                        .map(RequestMapper::toDto)
                        .toList(),

                rejected.stream()
                        .map(RequestMapper::toDto)
                        .toList()
        );
    }


    @Override
    public List<EventFullDto> getUserEvents(Long userId, int from, int size) {
        if (userId == null) {
            return List.of();
        }
        userService.getUserById(userId);

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());

        List<Event> events = eventRepository.findUserEvents(userId, pageable);

        return events.stream()
                .map(EventMapper::toDto)
                .toList();
    }


    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        userService.getUserById(userId);
        Event event = getEventById(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new InitiatorRequiredException("Пользователь не является инициатором события");
        }
        return EventMapper.toDto(event);
    }


    @Override
    public List<ParticipationRequestDto> getEventRequestsByInitiator(Long userId, Long eventId) {

        return requestService.getEventRequestsByInitiator(userId, eventId);
    }


    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID " + eventId + " не найдено"));
    }


//    A D M I N _ A P I

    @Override
    public EventFullDto updateAdminEvent(UpdateEventAdminRequest adminRequest, Long eventId) {
        Event event = getEventById(eventId);

        if (adminRequest.getStateAction() == AdminStateAction.PUBLISH_EVENT) {
            if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new EventStateException("До начала события остается менее 1 часа");
            }
            if (event.getState() != State.PENDING) {
                throw new EventStateException("Публикация возможна только в состоянии ожидания публикации");
            }
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }

        if (adminRequest.getStateAction() == AdminStateAction.REJECT_EVENT) {
            if (event.getState() == State.PUBLISHED) {
                throw new EventStateException("Нельзя отменить опубликованное событие");
            }
            event.setState(State.CANCELED);
        }

        if (adminRequest.getAnnotation() != null) {
            if (adminRequest.getAnnotation().length() < 20 || adminRequest.getAnnotation().length() > 2000) {
                throw new ValidationException("Аннотация должна содержать от 20 до 2000 символов");
            }
            event.setAnnotation(adminRequest.getAnnotation());
        }

        if (adminRequest.getCategory() != null) {
            Category category = categoryService.getCategoryById(adminRequest.getCategory());
            event.setCategory(category);
        }

        if (adminRequest.getDescription() != null) {
            if (adminRequest.getDescription().length() < 20 || adminRequest.getDescription().length() > 7000) {
                throw new ValidationException("Описание должно содержать от 20 до 7000 символов");
            }
            event.setDescription(adminRequest.getDescription());
        }

        if (adminRequest.getEventDate() != null) {
            if (adminRequest.getEventDate().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Дата уже состоялась");
            }
            if (adminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new EventModificationException("До начала события остаётся менее часа");
            }
            event.setEventDate(adminRequest.getEventDate());
        }

        if (adminRequest.getLocation() != null) {
            event.setLocation(adminRequest.getLocation());
        }

        if (adminRequest.getPaid() != null) {
            event.setPaid(adminRequest.getPaid());
        }

        if (adminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminRequest.getParticipantLimit());
        }

        if (adminRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminRequest.getRequestModeration());
        }
        
        if (adminRequest.getTitle() != null) {
            if (adminRequest.getTitle().length() < 3 || adminRequest.getTitle().length() > 120) {
                throw new ValidationException("Название должно содержать от 3 до 120 символов");
            }
            event.setTitle(adminRequest.getTitle());
        }
        eventRepository.save(event);

        return EventMapper.toDto(event);
    }


    @Override
    public List<EventFullDto> getEventsByParameters(List<Long> users, List<String> states, List<Long> categories,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    int from, int size) {

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());

        Specification<Event> spec = Specification
                .where(EventSpecifications.inUsers(users))
                .and(EventSpecifications.inStates(states))
                .and(EventSpecifications.inCategories(categories))
                .and(EventSpecifications.rangeStart(rangeStart))
                .and(EventSpecifications.rangeEnd(rangeEnd));

        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        return events.stream()
                .map(EventMapper::toDto)
                .toList();
    }


//    P U B L I C _ A P I

    @Override
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                               int from, int size, HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("Конец диапазона должен быть позже начала");
        }
        if (from < 0 || size < 0) {
            throw new ValidationException("Параметры пагинации не могут быть отрицательными");
        }

        Sort sortObj;

        if ("EVENT_DATE".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.DESC, "eventDate");
        } else if ("VIEWS".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.DESC, "views");
        } else {
            sortObj = Sort.unsorted();
        }

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<Event> spec = Specification
                .where(EventSpecifications.published())
                .and(EventSpecifications.textLike(text))
                .and(EventSpecifications.inCategories(categories))
                .and(EventSpecifications.paid(paid))
                .and(EventSpecifications.rangeStart(rangeStart))
                .and(EventSpecifications.rangeEnd(rangeEnd))
                .and(EventSpecifications.onlyAvailable(onlyAvailable));

        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .toList();

        Map<String, Long> viewsMap = new HashMap<>();

        if (!uris.isEmpty()) {
            List<ViewStats> stats = statsClient.getStats(LocalDateTime.now().minusYears(1), LocalDateTime.now(),
                    uris, true);

            viewsMap = stats.stream()
                    .collect(Collectors.toMap(ViewStats::getUri, ViewStats::getHits));
        }

        EndpointHitDto hit = new EndpointHitDto();
        hit.setApp("ewm-main-service");
        hit.setUri(request.getRequestURI().split("\\?")[0]);
        hit.setIp(request.getRemoteAddr());
        hit.setTimestamp(LocalDateTime.now());

        statsClient.saveHit(hit);

        List<EventShortDto> result = new ArrayList<>();

        for (Event event : events) {
            EventShortDto dto = EventMapper.toShortDto(event);

            Long views = viewsMap.getOrDefault("/events/" + event.getId(), 0L);
            dto.setViews(views);
            result.add(dto);
        }

        return result;
    }


    @Override
    public EventFullDto getPublicEventById(Long eventId, HttpServletRequest request) {
        Event event = getEventById(eventId);

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException(
                    "Событие с ID " + eventId + " не найдено");
        }

        String uri = "/events/" + eventId;
        List<ViewStats> stats = statsClient.getStats(LocalDateTime.now().minusYears(1), LocalDateTime.now(),
                List.of(uri), true);

        Long views = stats.isEmpty() ? 0 : stats.getFirst().getHits();

        EventFullDto dto = EventMapper.toDto(event);
        dto.setViews(views);

        EndpointHitDto hit = new EndpointHitDto();
        hit.setApp("ewm-main-service");
        hit.setUri(request.getRequestURI().split("\\?")[0]);
        hit.setIp(request.getRemoteAddr());
        hit.setTimestamp(LocalDateTime.now());

        statsClient.saveHit(hit);

        return dto;
    }
}