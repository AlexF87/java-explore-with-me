package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.dto.request.RequestShort;
import ru.practicum.explore_with_me.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventId(Long eventId);

    List<Request> findByIdInOrderByIdAsc(List<Long> requestId);

    List<Request> findAllByRequesterIdOrderByIdAsc(Long id);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);

    @Query("select r from Request as r where r.event.id = ?1 and r.requester.id = ?2")
    List<Request> findByEventAndRequester(Long event, Long userId);
    @Query("select new ru.practicum.explore_with_me.dto.request.RequestShort(count(r.id), r.event.id) " +
            "from Request r where r.event.id in (?1) " +
            "AND r.status = 'CONFIRMED' " +
            "group by r.event.id " +
            "order by count(r.id)")
    List<RequestShort> getRequestConfirmed(List<Long> eventId);

    @Query("select new ru.practicum.explore_with_me.dto.request.RequestShort(count(r.id), r.event.id) " +
            "from Request r where r.event.id = ?1 " +
            "AND r.status = 'CONFIRMED' " +
            "group by r.event.id " +
            "order by count(r.id)")
    RequestShort getRequestConfirmedOnlyOneEvent(Long eventId);
}
