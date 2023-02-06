package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import ru.practicum.explore_with_me.dto.StatsDtoRes;
import ru.practicum.explore_with_me.model.App;
import ru.practicum.explore_with_me.model.Stat;
import ru.practicum.explore_with_me.model.StatShort;

@Component
public class StatsMapper {
    public Stat toStat(StatsDtoRequest statsDtoRequest, App app) {
        Stat stat = new Stat();
        stat.setApp(app);
        stat.setUri(statsDtoRequest.getUri());
        stat.setIp(statsDtoRequest.getIp());
        stat.setTimestamp(statsDtoRequest.getTimestamp());
        return stat;
    }

    public static StatsDtoRes toStatsDtoRes(StatShort statShort) {
        StatsDtoRes statsDtoRes = new StatsDtoRes();
        statsDtoRes.setApp(statShort.getApp().getApp());
        statsDtoRes.setUri(statShort.getUri());
        statsDtoRes.setHits(statShort.getHits());
        return statsDtoRes;
    }
}
