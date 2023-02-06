package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.StatsDtoRes;
import ru.practicum.explore_with_me.model.StatShort;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import ru.practicum.explore_with_me.mapper.StatsMapper;
import ru.practicum.explore_with_me.model.App;
import ru.practicum.explore_with_me.model.Stat;
import ru.practicum.explore_with_me.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsMapper mapper;
    private final StatsRepository statsRepository;
    private final AppService appService;

    @Override
    @Transactional
    public void add(StatsDtoRequest statsDto) {
        App app = appService.findByApp(statsDto.getApp());
        Stat stat = mapper.toStat(statsDto, app);
        statsRepository.save(stat);
    }

    @Override
    public List<StatsDtoRes> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<StatShort> statShortList = new ArrayList<>();
        if (unique) {
            if (uris.isEmpty()) {
                statShortList = statsRepository.getStatUniqueUriIsEmpty(start, end);
                return statShortList.stream().map(StatsMapper::toStatsDtoRes).collect(Collectors.toList());
            }
            statShortList = statsRepository.getStatUnique(start, end, uris);
            return statShortList.stream().map(StatsMapper::toStatsDtoRes).collect(Collectors.toList());

        } else {
            if (uris.isEmpty()) {
                statShortList = statsRepository.getStatUrisIsEmpty(start, end);
                return statShortList.stream().map(StatsMapper::toStatsDtoRes).collect(Collectors.toList());
            }
            statShortList = statsRepository.getStat(start, end, uris);
            return statShortList.stream().map(StatsMapper::toStatsDtoRes).collect(Collectors.toList());
        }
   }
}
