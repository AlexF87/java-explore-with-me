package ru.practicum.explore_with_me;

import org.springframework.web.client.RestTemplate;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ClientStats {
    private RestTemplate restTemplate;
    private String url = "//stats-service:9090";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public void saveStat(StatsDtoRequest stat) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsDtoRequest> requestEntity = new HttpEntity<>(stat, headers);
        restTemplate = new RestTemplate();
        restTemplate.exchange(url + "/hit", HttpMethod.POST, requestEntity, StatsDtoRequest.class);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        StringBuilder urisFull = new StringBuilder();
        for (int i = 0; i < uris.length; i++) {
            if (i < (uris.length - 1)) {
                urisFull.append("uris").append("=").append(uris[i]).append(",");
            } else {
                urisFull.append("uris").append("=").append(uris[i]);
            }
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", urisFull.toString(),
                "unique", unique);

        restTemplate = new RestTemplate();
        String uri = url + "/stats?start=" + start + "&end=" + end + "&" + uris + "&" + unique;
        return restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Object.class, parameters);
    }
}
