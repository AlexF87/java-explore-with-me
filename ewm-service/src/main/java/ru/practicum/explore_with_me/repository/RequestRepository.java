package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request>findAllByEvent(Long eventId);
    List<Request>findByIdInOrderByIdAsc(List<Long> requestId);

    List<Request>findAllByRequesterOrderByIdAsc(Long id);

    Optional<Request> findByEventAndRequester(Long event, Long userId);

    Optional<Request> findByIdAndRequester(Long requestId, Long userId);
}
