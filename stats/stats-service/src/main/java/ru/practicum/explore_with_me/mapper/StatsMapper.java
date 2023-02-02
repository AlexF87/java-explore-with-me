package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import ru.practicum.explore_with_me.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {
    public Stat toStat(StatsDtoRequest statsDtoRequest) {
        Stat stat = new Stat();
        stat.setApp(statsDtoRequest.getApp());
        stat.setUri(statsDtoRequest.getUri());
        stat.setIp(statsDtoRequest.getIp());
        stat.setTimestamp(LocalDateTime.parse(statsDtoRequest.getTimestamp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return stat;
    }
}
