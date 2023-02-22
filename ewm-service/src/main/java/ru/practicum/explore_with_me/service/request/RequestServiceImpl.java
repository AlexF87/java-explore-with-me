package ru.practicum.explore_with_me.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.dto.request.RequestShort;
import ru.practicum.explore_with_me.dto.request.RequestState;
import ru.practicum.explore_with_me.handler.exception.ForbiddenExceptionCust;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.RequestMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.Request;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.RequestRepository;
import ru.practicum.explore_with_me.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private static final int WITH_NANO_ZERO = 0;

    @Override
    public List<ParticipationRequestDto> findRequestEvent(Long eventId) {
        List<Request> result = requestRepository.findAllByEventId(eventId);
        return result.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> findRequestForUpdate(List<Long> requestId) {
        List<Request> result = requestRepository.findByIdInOrderByIdAsc(requestId);
        return result.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByUserId(Long userId) {
        userService.checkUser(userId);
        List<Request> result = requestRepository.findAllByRequesterIdOrderByIdAsc(userId);
        return result.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User user = userService.checkUser(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        event.setConfirmedRequests(getConfirmedRequest(eventId));
        checkRepeatRequest(eventId, userId);
        if (userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenExceptionCust("The initiator adds the request to its event.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ForbiddenExceptionCust("Event not published");
        }
        long limitRequest = event.getParticipantLimit() - event.getConfirmedRequests();
        if (limitRequest == 0) {
            throw new ForbiddenExceptionCust("The limit of participation requests has been reached");
        }
        Request newRequest = new Request();
        if (!event.getRequestModeration()) {
            newRequest.setStatus(RequestState.CONFIRMED);
            eventRepository.save(event);
        } else {
            newRequest.setStatus(RequestState.PENDING);
        }
        newRequest.setRequester(user);
        newRequest.setCreated(LocalDateTime.now().withNano(WITH_NANO_ZERO));
        newRequest.setEvent(event);

        return RequestMapper.toParticipationRequestDto(requestRepository.save(newRequest));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequestById(Long userId, Long requestId) {
        userService.checkUser(userId);
        Request request = checkRequest(requestId, userId);
        Event event = request.getEvent();
        eventRepository.save(event);
        request.setStatus(RequestState.CANCELED);
        request = requestRepository.save(request);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<RequestShort> getRequestShort(List<Long> idEvents) {
        return requestRepository.getRequestConfirmed(idEvents);
    }

    @Override
    @Transactional
    public void updateRequest(long idRequest, RequestState status) {
        Optional<Request> request = requestRepository.findById(idRequest);
        if (request.isPresent()) {
            request.get().setStatus(status);
            requestRepository.save(request.get());
        }
    }

    private long getConfirmedRequest(Long idEvent) {
        RequestShort requestShort = requestRepository.getRequestConfirmedOnlyOneEvent(idEvent);
        if (requestShort != null) {
            return requestShort.getConfirmedRequests();
        }
        return 0L;
    }

    private void checkRepeatRequest(Long event, Long userId) {
        List<Request> request = requestRepository.findByEventAndRequester(event, userId);
        if (request.size() > 0) {
            throw new ForbiddenExceptionCust(
                    String.format("You cannot add a repeat request, eventId %d, userId", event, userId));
        }
    }

    private Request checkRequest(Long requestId, Long userId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() ->
                new NotFoundException(String.format("Request with id=%d was not found", requestId)));
        return request;
    }
}
