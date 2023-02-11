package ru.practicum.explore_with_me.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.mapper.RequestMapper;
import ru.practicum.explore_with_me.model.Request;
import ru.practicum.explore_with_me.repository.RequestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> findRequestEvent(Long eventId) {
        List<Request> result = requestRepository.findByEventAndOrderByIdAsc(eventId);

        return result.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }
}
