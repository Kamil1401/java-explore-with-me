package ru.practicum;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class StatsClient {
    private final RestTemplate restTemplate;


    public StatsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void saveHit(EndpointHitDto dto) {
        restTemplate.postForLocation("/hit", dto);
    }


    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", uris.toArray());
        }

        ResponseEntity<ViewStats[]> response = restTemplate.getForEntity(builder.toUriString(), ViewStats[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}