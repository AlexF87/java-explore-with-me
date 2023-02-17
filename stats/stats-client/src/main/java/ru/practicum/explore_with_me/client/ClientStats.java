package ru.practicum.explore_with_me.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import org.springframework.http.*;
import ru.practicum.explore_with_me.dto.StatsDtoRes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Component
@Slf4j
public class ClientStats {
    @Value("${stats-service-url}")
    private String statsServer;
    private String API_PREFIX_HIT = "hit";

    private String API_PREFIX_STATS = statsServer + "stats";

    private final RestTemplate restTemplate;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void saveStat(StatsDtoRequest stat) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsDtoRequest> requestEntity = new HttpEntity<>(stat, headers);
        log.info(String.format("%S", API_PREFIX_HIT));
        restTemplate.exchange(statsServer + "hit", HttpMethod.POST, requestEntity, StatsDtoRequest
                .class);
    }

    public ResponseEntity<List<StatsDtoRes>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                                      Boolean unique) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        StringBuilder urisFull = new StringBuilder();
        for (int i = 0; i < uris.size(); i++) {
            if (i < (uris.size() - 1)) {
                urisFull.append("uris").append("=").append(uris.get(i)).append(",");
            } else {
                urisFull.append("uris").append("=").append(uris.get(i));
            }
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", urisFull.toString(),
                "unique", unique);

        String url = statsServer + "stats" + "?start={start}&end={end}&uris={uris}&unique={unique}";
        ResponseEntity<List<StatsDtoRes>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<StatsDtoRes>>() {
                },
                parameters);
        return response;
    }
}
