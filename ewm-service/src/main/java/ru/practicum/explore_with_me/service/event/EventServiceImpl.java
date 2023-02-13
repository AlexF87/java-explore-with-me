package ru.practicum.explore_with_me.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.common.CustomPageRequest;
import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.dto.request.RequestState;
import ru.practicum.explore_with_me.handler.exception.BadRequestException;
import ru.practicum.explore_with_me.handler.exception.ForbiddenException;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.Request;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.category.CategoryService;
import ru.practicum.explore_with_me.service.request.RequestService;
import ru.practicum.explore_with_me.service.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final RequestService requestService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EntityManager em;

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
        List<EventDtoResponse> result = page.stream().map(EventMapper::toEventResponse).collect(Collectors.toList());
        return result;
    }

    @Override
    public EventDtoResponse updateEventAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = checkEvent(id);
        checkActionState(event, updateEventAdminRequest);
        checkDate(event, updateEventAdminRequest);
        event = updateEventAdm(event, updateEventAdminRequest);
        event = eventRepository.save(event);
        return EventMapper.toEventResponse(event);
    }

    @Override
    public List<EventShortDto> findAllEvents(String text, List<Long> categories, Boolean paid,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Boolean onlyAvailable, String sort, Integer from,
                                             Integer size) {
        Sort sortEvent = Sort.by("id").ascending();
        if (sort != null && sort.equals("EVENT_DATE")) {
            sortEvent = Sort.by("eventDate").ascending();
        } else if (sort != null && sort.equals("VIEWS")) {
            sortEvent = Sort.by("views").ascending();
        }
        Pageable pageable = CustomPageRequest.of(from, size, sortEvent);
       /* CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = builder.createQuery(Event.class);

        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (text != null) {
            Path<String> pathAnnot = root.get("annotation");
            Expression<String> path = root.get("annotation");
            Expression<String> pathLower = builder.lower(path);
            Expression<String> pathDes = root.get("description");
            Expression<String> pathLowerDes = builder.lower(pathDes);
            Predicate likeText = builder.like(pathLower, "%" + text.toLowerCase() + "%");
            Predicate likeTextDes = builder.like(pathLowerDes, "%" + text.toLowerCase() + "%");
            predicates.add(likeText);
            predicates.add(likeTextDes);
        }
        if (categories != null && !categories.isEmpty()) {
            Path<Long> pathCat = root.get("category");
            Predicate inCat = pathCat.in(categories);
            predicates.add(inCat);
        }
        if (paid != null) {
            Path<Boolean> pathPaid = root.get("paid");
            Predicate paidFilter = pathPaid.in(paid);
            predicates.add(paidFilter);
        }
        if (rangeStart != null && rangeEnd != null) {
            Predicate date = builder.between(root.get("eventDate"), rangeStart, rangeEnd);
            predicates.add(date);
        } else {
            LocalDateTime now = LocalDateTime.now();
            Predicate date = builder.greaterThan(root.get("eventDate"), now);
            predicates.add(date);
        }
        if (onlyAvailable != null) {
            Expression<Long> param = root.get("confirmedRequests");
            Expression<Long> param2 = root.get("participantLimit");
            Predicate limitFilter = builder.greaterThan(param2, param);
            predicates.add(limitFilter);
        }
        Predicate stateFilter = builder.equal(root.get("state"), EventState.PUBLISHED);
        predicates.add(stateFilter);
        cq.select(root).where(builder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<Event> query = em.createQuery(cq);
        Page<Event> page = new PageImpl<Event>(query.getResultList(), pageable, size);*/
        Page<Event> list = eventRepository.findByParams(text.toLowerCase(), categories, paid, rangeStart, rangeEnd,
                onlyAvailable, pageable);
        return list.stream().map(EventMapper::toEvenShortDto).collect(Collectors.toList());
    }

    @Override
    public EventDtoResponse getEvent(Long id) {
        Event event = checkEvent(id);
        if (event.getState() != EventState.PUBLISHED) {
            throw new BadRequestException("Event must be published");
        }
        return EventMapper.toEventResponse(event);
    }

    @Override
    public List<EventShortDto> getEventsForPrivate(Long userId, Integer from, Integer size) {
        userService.checkUser(userId);
        List<Event> result = new ArrayList<>();
        Pageable pageable = CustomPageRequest.of(from, size, Sort.by("id").ascending());
        result = eventRepository.findAllEventInitiatorWithPagination(userId, pageable);
        return result.stream().map(EventMapper::toEvenShortDto).collect(Collectors.toList());
    }

    @Override
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
        return EventMapper.toEventResponse(event);
    }

    @Override
    public EventDtoResponse updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userService.checkUser(userId);
        Event event = checkEvent(eventId);
        Category category = null;
        if (updateEventUserRequest.getCategory() != null) {
            category = categoryService.checkCategory(updateEventUserRequest.getCategory());
        }
        if (!Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new ForbiddenException("User is not initiator");
        }
        if (event.getState().equals(EventState.PUBLISHED.toString())) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
        LocalDateTime now = LocalDateTime.now();
        if (updateEventUserRequest.getEventDate() != null) {
            now.plusHours(2);
            if (updateEventUserRequest.getEventDate().isBefore(now)) {
                throw new ForbiddenException("event cannot be earlier than two hours from the current moment");
            }
        }
        event = updateEventUser(event, updateEventUserRequest, category);
        return EventMapper.toEventResponse(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsEvent(Long userId, Long eventId) {
        List<ParticipationRequestDto> result = new ArrayList<>();
        userService.checkUser(userId);
        checkEvent(eventId);
        result = requestService.findRequestEvent(eventId);
        return result;
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequest) {
        User user = userService.checkUser(userId);
        Event event = checkEvent(eventId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return result;
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ForbiddenException("The limit on applications for this event has been reached");
        }
        List<ParticipationRequestDto> requests = requestService.findRequestForUpdate(eventRequest.getRequestIds());
        for (ParticipationRequestDto request : requests) {
            if (request.getStatus() != RequestState.PENDING) {
                throw new ForbiddenException("the request is not in a PENDING state");
            }
            if (checkParticipantLimit(event) && eventRequest.getStatus() == RequestState.CONFIRMED) {
                long confirmed = event.getConfirmedRequests();
                event.setConfirmedRequests(++confirmed);
                request.setStatus(RequestState.CONFIRMED);
                result.getConfirmedRequests().add(request);

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

    private Event checkEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Not found event, id: %d ", id)));
    }

    private void checkActionState(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getStateAction() == StateAction.PUBLISH_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new ForbiddenException("Cannot publish the event because it's not in the right state: PUBLISHED");
            }
        }
        if (updateEvent.getStateAction() == StateAction.REJECT_EVENT) {
            if (event.getState() == EventState.PUBLISHED) {
                throw new ForbiddenException("Event not PUBLISHED");
            }
        }
    }

    private void checkDate(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getEventDate() != null && event.getPublishedOn() != null) {
            if (updateEvent.getEventDate().isBefore(event.getPublishedOn().plusMinutes(60))) {
                throw new ForbiddenException("Wrong date. The start date of the event should not be earlier than " +
                        "an hour from the date of publication.");
            }
        }
    }

    private Event updateEventAdm(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.getCategory().setId(updateEvent.getCategory());
        }
        if (updateEvent.getDescription() != null) {
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
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }

    private void checkWhenCreate(NewEventDto newEventDto) {
        LocalDateTime now = LocalDateTime.now();
        now.plusHours(2);
        if (!newEventDto.getEventDate().isAfter(now)) {
            throw new ForbiddenException(String.format("Field: eventDate. Error: должно содержать дату, которая еще не " +
                    "наступила.Value: %s", newEventDto.getEventDate().toString()));
        }

    }

    private Event updateEventUser(Event event, UpdateEventUserRequest updateEvent, Category category) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
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
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }
}
