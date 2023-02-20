package ru.practicum.explore_with_me.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.client.ClientStats;
import ru.practicum.explore_with_me.common.CustomPageRequest;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import ru.practicum.explore_with_me.dto.StatsDtoRes;
import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.dto.request.*;
import ru.practicum.explore_with_me.handler.exception.BadRequestException;
import ru.practicum.explore_with_me.handler.exception.ForbiddenExceptionCust;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.handler.exception.StatsException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.category.CategoryService;
import ru.practicum.explore_with_me.service.request.RequestService;
import ru.practicum.explore_with_me.service.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final RequestService requestService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EntityManager em;
    private final ClientStats clientStats;
    @Value("${name-app}")
    private String nameApp;

    @Override
    public List<EventDtoResponse> getEventsAdmin(List<Long> usersId, List<EventState> states, List<Long> categoriesId,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                 Integer size) {
        Pageable pageable = CustomPageRequest.of(from, size, Sort.by("createdOn").descending());
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = builder.createQuery(Event.class);

        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (usersId != null && !usersId.isEmpty()) {
            Path<String> userPath = root.get("initiator");
            Predicate inUserId = userPath.in(usersId);
            predicates.add(inUserId);
        }
        if (states != null && !states.isEmpty()) {
            Path<String> statePath = root.get("state");
            Predicate inState = statePath.in(states);
            predicates.add(inState);
        }
        if (categoriesId != null && !categoriesId.isEmpty()) {
            Path<String> catPath = root.get("category");
            Predicate inCat = catPath.in(categoriesId);
            predicates.add(inCat);
        }
        if (rangeStart != null && rangeEnd != null) {
            Predicate date = builder.between(root.get("eventDate"), rangeStart, rangeEnd);
            predicates.add(date);
        }
        cq.select(root);
        cq.where(builder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<Event> query = em.createQuery(cq);
        Page<Event> page = new PageImpl<Event>(query.getResultList(), pageable, size);
        writeViews(page.getContent());
        List<EventDtoResponse> result = page.stream().map(EventMapper::toEventResponse).collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional
    public EventDtoResponse updateEventAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = getEventOrThrow(id);
        checkActionState(event, updateEventAdminRequest);
        checkDate(event, updateEventAdminRequest);
        event = updateEventAdm(event, updateEventAdminRequest);
        event = eventRepository.save(event);
        writeViews(List.of(event));
        return EventMapper.toEventResponse(event);
    }

    @Override
    @Transactional
    public List<EventShortDto> findAllEvents(String text, List<Long> categories, Boolean paid,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Boolean onlyAvailable, String sort, Integer from,
                                             Integer size, HttpServletRequest request) {
        Sort sortEvent = Sort.by("id").ascending();
        if (sort != null && sort.equals("EVENT_DATE")) {
            sortEvent = Sort.by("eventDate").ascending();
        } else if (sort != null && sort.equals("VIEWS")) {
            sortEvent = Sort.by("views").ascending();
        }
        updateStat(request);
        Pageable pageable = CustomPageRequest.of(from, size, sortEvent);
        String lowerText = null;
        if (text != null) {
            lowerText = text.toLowerCase();
        }
        Page<Event> list = eventRepository.findByParams(lowerText, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, pageable);
        writeViews(list.getContent());
        return list.stream().map(EventMapper::toEvenShortDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDtoResponse getEvent(Long id, HttpServletRequest request) {
        updateStat(request);
        Event event = getEventOrThrow(id);
        if (event.getState() != EventState.PUBLISHED) {
            throw new BadRequestException("Event must be published");
        }
        writeViews(List.of(event));
        return EventMapper.toEventResponse(event);
    }

    @Override
    public List<EventShortDto> getEventsForPrivate(Long userId, Integer from, Integer size) {
        userService.checkUser(userId);
        List<Event> result = new ArrayList<>();
        Pageable pageable = CustomPageRequest.of(from, size, Sort.by("id").ascending());
        result = eventRepository.findAllEventInitiatorWithPagination(userId, pageable);
        writeViews(result);
        return result.stream().map(EventMapper::toEvenShortDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDtoResponse addEvent(Long userId, NewEventDto newEventDto) {
        User user = userService.checkUser(userId);
        Category category = categoryService.checkCategory(newEventDto.getCategory());
        checkWhenCreate(newEventDto);
        Event event = eventMapper.toEvent(newEventDto, user, category);
        return EventMapper.toEventResponse(eventRepository.save(event));
    }

    @Override
    public EventDtoResponse findUserEvent(Long userId, Long eventId) {
        userService.checkUser(userId);
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        if (event == null) {
            throw new NotFoundException(String.format("Event with id=%d was not found", eventId));
        }
        writeViews(List.of(event));
        return EventMapper.toEventResponse(event);
    }

    @Override
    @Transactional
    public EventDtoResponse updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userService.checkUser(userId);
        Event event = getEventOrThrow(eventId);
        Category category = null;
        if (updateEventUserRequest.getCategory() != null) {
            category = categoryService.checkCategory(updateEventUserRequest.getCategory());
        }
        if (!Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new ForbiddenExceptionCust("User is not initiator");
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenExceptionCust("Only pending or canceled events can be changed");
        }
        LocalDateTime now = LocalDateTime.now();
        if (updateEventUserRequest.getEventDate() != null) {
            now.plusHours(2);
            if (updateEventUserRequest.getEventDate().isBefore(now)) {
                throw new ForbiddenExceptionCust("event cannot be earlier than two hours from the current moment");
            }
        }
        event = updateEventUser(event, updateEventUserRequest, category);
        writeViews(List.of(event));
        return EventMapper.toEventResponse(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsEvent(Long userId, Long eventId) {
        List<ParticipationRequestDto> result = new ArrayList<>();
        userService.checkUser(userId);
        getEventOrThrow(eventId);
        result = requestService.findRequestEvent(eventId);
        return result;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequest) {
        User user = userService.checkUser(userId);
        Event event = getEventOrThrow(eventId);
        writeConfirmedRequests(List.of(event));
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return result;
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ForbiddenExceptionCust("The limit on applications for this event has been reached");
        }
        List<ParticipationRequestDto> requests = requestService.findRequestForUpdate(eventRequest.getRequestIds());
        for (ParticipationRequestDto request : requests) {
            if (request.getStatus() != RequestState.PENDING) {
                throw new ForbiddenExceptionCust("the request is not in a PENDING state");
            }
            if (checkParticipantLimit(event) && eventRequest.getStatus() == RequestState.CONFIRMED) {
                request.setStatus(RequestState.CONFIRMED);
                result.getConfirmedRequests().add(request);
                requestService.updateRequest(request.getId(), RequestState.CONFIRMED);
                writeConfirmedRequests(List.of(event));
            } else {
                request.setStatus(RequestState.REJECTED);
                result.getRejectedRequests().add(request);
            }

        }
        return result;
    }

    private boolean checkParticipantLimit(Event event) {
        long x = event.getParticipantLimit() - event.getConfirmedRequests();
        return x > 0 ? true : false;
    }

    @Override
    public Event getEventOrThrow(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Not found event, id: %d ", id)));
    }

    private void checkActionState(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getStateAction() == StateAction.PUBLISH_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new ForbiddenExceptionCust("Cannot publish the event because it's not in the right state: PUBLISHED");
            }
        }
        if (updateEvent.getStateAction() == StateAction.REJECT_EVENT) {
            if (event.getState() == EventState.PUBLISHED) {
                throw new ForbiddenExceptionCust("Event not PUBLISHED");
            }
        }
        if (event.getState().equals(EventState.CANCELED)
                && updateEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            throw new ForbiddenExceptionCust("Publishing a canceled event");
        }
    }

    private void checkDate(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getEventDate() != null && event.getPublishedOn() != null) {
            if (updateEvent.getEventDate().isBefore(event.getPublishedOn().plusMinutes(60))) {
                throw new ForbiddenExceptionCust("Wrong date. The start date of the event should not be earlier than " +
                        "an hour from the date of publication.");
            }
        }
        if (updateEvent.getEventDate() != null && updateEvent.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ForbiddenExceptionCust("The event does not meet the editing rules");
        }
    }

    private Event updateEventAdm(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.getCategory().setId(updateEvent.getCategory());
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (StateAction.CANCEL_REVIEW.equals(updateEvent.getStateAction()) ||
                StateAction.REJECT_EVENT.equals(updateEvent.getStateAction())) {
            event.setState(EventState.CANCELED);
        }
        if (StateAction.SEND_TO_REVIEW.equals(updateEvent.getStateAction())) {
            event.setState(EventState.PENDING);
        }
        if (StateAction.PUBLISH_EVENT.equals(updateEvent.getStateAction())) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }

    private void checkWhenCreate(NewEventDto newEventDto) {
        LocalDateTime now = LocalDateTime.now();
        now.plusHours(2);
        if (!newEventDto.getEventDate().isAfter(now)) {
            throw new ForbiddenExceptionCust(String.format("Field: eventDate. Error: должно содержать дату, которая еще не " +
                    "наступила.Value: %s", newEventDto.getEventDate().toString()));
        }

    }

    private Event updateEventUser(Event event, UpdateEventUserRequest updateEvent, Category category) {
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (StateAction.CANCEL_REVIEW.equals(updateEvent.getStateAction())) {
            event.setState(EventState.CANCELED);
        }
        if (StateAction.SEND_TO_REVIEW.equals(updateEvent.getStateAction())) {
            event.setState(EventState.PENDING);
        }
        if (StateAction.PUBLISH_EVENT.equals(updateEvent.getStateAction())) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }

    private void updateStat(HttpServletRequest request) {
        try {
            StatsDtoRequest statsDtoRequest = new StatsDtoRequest();
            statsDtoRequest.setApp(nameApp);
            statsDtoRequest.setIp(request.getRemoteAddr());
            statsDtoRequest.setUri(request.getRequestURI());
            statsDtoRequest.setTimestamp(LocalDateTime.now());
            clientStats.saveStat(statsDtoRequest);
            log.info("Sending statistics was successful");
        } catch (StatsException e) {
            log.error("Sending statistics failed");
        }
    }

    private void writeViews(List<Event> events) {
        writeConfirmedRequests(events);
        List<String> uris = new ArrayList<>();
        events.forEach((e) ->
                uris.add("/events/" + e.getId())
        );
        ResponseEntity<List<StatsDtoRes>> response;
        List<StatsDtoRes> stats = new ArrayList<>();
        try {
            response = clientStats.getStats(LocalDateTime.of(2000, 1, 1,
                    0, 0), LocalDateTime.now(), uris, false);

            stats = response.getBody();
        } catch (StatsException e) {
            log.error(String.format("client-stat: %s", e.getMessage()));
        }
        if (stats != null && !stats.isEmpty()) {
            for (Event e : events) {
                for (StatsDtoRes dto : stats) {
                    String[] words = dto.getUri().split("/");
                    if (Integer.parseInt(words[2]) == e.getId()) {
                        e.setViews(dto.getHits());
                    }
                }
            }
        } else {
            events.forEach(e -> e.setViews(0L));
        }
    }

    private void writeConfirmedRequests(List<Event> events) {
        if (events != null && !events.isEmpty()) {
            List<Long> eventId = new ArrayList<>();
            events.forEach(
                    e -> {
                        eventId.add(e.getId());
                    }
            );
            List<RequestShort> result = requestService.getRequestShort(eventId);
            if (result != null && !result.isEmpty()) {
                for (RequestShort requestShort : result) {
                    for (Event event : events) {
                        if (requestShort.getId() == event.getId()) {
                            event.setConfirmedRequests(requestShort.getConfirmedRequests());
                        }
                    }
                }
            }
            events.forEach(e -> {
                        if (e.getConfirmedRequests() == null)
                            e.setConfirmedRequests(0L);
                    }
            );
        }
    }
}
