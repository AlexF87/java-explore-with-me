package ru.practicum.explore_with_me.service.request;

import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> findRequestEvent(Long eventId);

    List<ParticipationRequestDto> findRequestForUpdate(List<Long> requestId);

    List<ParticipationRequestDto> getRequestsByUserId(Long userId);

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestById(Long userId, Long requestId);
}
