package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.model.StatShort;
import ru.practicum.explore_with_me.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {
    @Query("select new ru.practicum.explore_with_me.model.StatShort(s.app, s.uri, count(distinct s.ip)) "
            + " from Stat s "
            + " where s.timestamp between ?1 and ?2 and s.uri in (?3) "
            + " group by s.app, s.uri"
            + " order by count(distinct s.ip) desc")
    List<StatShort> getStatUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.explore_with_me.model.StatShort(s.app, s.uri, count(distinct s.ip)) "
            + " from Stat s "
            + " where s.timestamp between ?1 and ?2 "
            + " group by s.app, s.uri"
            + " order by count(distinct s.ip) desc")
    List<StatShort> getStatUniqueUriIsEmpty(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explore_with_me.model.StatShort(s.app, s.uri, count(s.ip)) "
            + " from Stat s "
            + " where s.timestamp between ?1 and ?2 and s.uri in (?3) "
            + " group by s.app, s.uri "
            + " order by count(s.ip) desc")
    List<StatShort> getStat(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.explore_with_me.model.StatShort(s.app, s.uri, count(s.ip)) "
         + " from Stat s "
         + " where s.timestamp between ?1 and ?2 "
         + " group by s.app, s.uri "
         + " order by count(s.ip) desc")
    List<StatShort> getStatUrisIsEmpty(LocalDateTime start, LocalDateTime end);
}
