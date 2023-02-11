package ru.practicum.explore_with_me.service.event;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.common.CustomPageRequest;
import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.event.StateAction;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.handler.exception.ForbiddenException;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.EventRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EntityManager em;

    @Override
    public List<EventDtoResponse> getEventsAdmin(List<Long> usersId, List<EventState> states, List<Long> categoriesId,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                 Integer size) {
        Pageable pageable = CustomPageRequest.of(from, size, Sort.by("id").ascending());
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Event> critQuery = builder.createQuery(Event.class);

        Root<Event> root = critQuery.from(Event.class);
        critQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (usersId != null && !usersId.isEmpty()) {
            Expression<Long> exp = root.get("id");
            Predicate inUserId = exp.in(usersId);
            predicates.add(inUserId);
        }
        if (states != null && !states.isEmpty()) {
            Expression<EventState> st = root.get("state");
            Predicate inState = st.in(states);
            predicates.add(inState);
        }
        if (categoriesId != null && !categoriesId.isEmpty()) {
            Root<Category> root2 = critQuery.from(Category.class);
            Expression<Long> cat = root2.get("id");
            Predicate inCat = cat.in(categoriesId);
            predicates.add(inCat);
        }
        if (rangeStart != null && rangeEnd != null) {
            Predicate date = builder.between(root.get("eventDate"), rangeStart, rangeEnd);
            predicates.add(date);
        }
        critQuery.select(root);
        critQuery.where(builder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<Event> query = em.createQuery(critQuery);
        Page<Event> page = new PageImpl<Event>(query.getResultList(), pageable, size);
        List<EventDtoResponse> result = page.stream().map(EventMapper::toEventResponse).collect(Collectors.toList());
        return result;
    }

    @Override
    public EventDtoResponse updateEventAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = checkEvent(id);
        checkActionState(event, updateEventAdminRequest);
        event = updateEvent(event, updateEventAdminRequest);
        event = eventRepository.save(event);
        return eventMapper.toEventResponse(event);
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

    private Event updateEvent(Event event, UpdateEventAdminRequest updateEvent) {
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
}
