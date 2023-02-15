package ru.practicum.explore_with_me.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.dto.request.RequestState;
import ru.practicum.explore_with_me.handler.exception.ForbiddenException;
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
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User user = userService.checkUser(userId);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException(String.format("Event with id=%d was not found", eventId));
        }
        checkRepeatRequest(eventId, userId);
        if (userId == event.get().getInitiator().getId()) {
            throw new ForbiddenException("The initiator adds the request to its event.");
        }
        if (event.get().getState() != EventState.PUBLISHED) {
            throw new ForbiddenException("Event not published");
        }
        long limitRequest = event.get().getParticipantLimit() - event.get().getConfirmedRequests();
        if (limitRequest == 0) {
            throw new ForbiddenException("The limit of participation requests has been reached");
        }
        Request newRequest = new Request();
        if (!event.get().getRequestModeration()) {
            newRequest.setStatus(RequestState.CONFIRMED);
            long confReq = event.get().getConfirmedRequests();
            event.get().setConfirmedRequests(++confReq);
            eventRepository.save(event.get());
        } else {
            newRequest.setStatus(RequestState.PENDING);
        }
        newRequest.setRequester(user);
        newRequest.setCreated(LocalDateTime.now().withNano(WITH_NANO_ZERO));
        newRequest.setEvent(event.get());

        return RequestMapper.toParticipationRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public ParticipationRequestDto cancelRequestById(Long userId, Long requestId) {
        userService.checkUser(userId);
        Request request = checkRequest(requestId, userId);
        Event event = request.getEvent();
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);
        request.setStatus(RequestState.CANCELED);
        request = requestRepository.save(request);
        return RequestMapper.toParticipationRequestDto(request);
    }

    private void checkRepeatRequest(Long event, Long userId) {
        List<Request> request = requestRepository.findByEventAndRequester(event, userId);
        if (request.size() > 0) {
            throw new ForbiddenException(
                    String.format("You cannot add a repeat request, eventId %d, userId", event, userId));
        }
    }

    private Request checkRequest(Long requestId, Long userId) {
        Optional<Request> request = requestRepository.findByIdAndRequesterId(requestId, userId);
        request.orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));
        return request.get();
    }
}
