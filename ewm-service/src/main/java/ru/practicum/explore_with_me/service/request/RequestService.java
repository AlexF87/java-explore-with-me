package ru.practicum.explore_with_me.service.request;

import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.dto.request.RequestShort;
import ru.practicum.explore_with_me.dto.request.RequestState;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> findRequestEvent(Long eventId);

    List<ParticipationRequestDto> findRequestForUpdate(List<Long> requestId);

    List<ParticipationRequestDto> getRequestsByUserId(Long userId);

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestById(Long userId, Long requestId);

    List<RequestShort> getRequestShort(List<Long> idEvents);

    void updateRequest(long idRequest, RequestState status);
}
