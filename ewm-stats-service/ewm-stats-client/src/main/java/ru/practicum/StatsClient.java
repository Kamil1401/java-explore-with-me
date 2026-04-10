package ru.practicum;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class StatsClient {
    private final RestTemplate restTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public StatsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void saveHit(EndpointHitDto dto) {
        restTemplate.postForLocation("/hit", dto);
    }


    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        StringBuilder url = new StringBuilder("/stats");

        url.append("?start=").append(encode(start.format(FORMATTER)));
        url.append("&end=").append(encode(end.format(FORMATTER)));
        url.append("&unique=").append(unique);

        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                url.append("&uris=").append(encode(uri));
            }
        }
        ResponseEntity<ViewStats[]> response = restTemplate.getForEntity(url.toString(), ViewStats[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }


    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}