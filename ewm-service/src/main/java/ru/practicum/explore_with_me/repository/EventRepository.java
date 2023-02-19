package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByInitiatorIdAndId(Long userId, Long eventId);

    @Query(value = "SELECT e FROM Event e WHERE e.initiator.id = ?1")
    List<Event> findAllEventInitiatorWithPagination(Long id, Pageable pageable);

    @Query("select e " +
            "from Event e " +
            "where " +
            "(:text is null OR ((lower(e.annotation) LIKE :text OR lower(e.description) LIKE :text))) " +
            "AND (:cat is null OR e.category.id IN :cat) " +
            "AND (:paid is null OR e.paid = :paid) " +
            "AND (e.eventDate BETWEEN :start AND :end) " +
            "AND (:onlyAvailable is null OR e.participantLimit > (select count(r) from" +
            " Request  r where  e.id = r.event.id AND r.status = 'CONFIRMED') )")
    Page<Event> findByParams(@Param("text") String text, @Param("cat") List<Long> cat,
                             @Param("paid") Boolean paid, @Param("start") LocalDateTime rangeStart,
                             @Param("end") LocalDateTime rangeEnd, @Param("onlyAvailable") Boolean onlyAvailable,
                             Pageable pageable);
}
