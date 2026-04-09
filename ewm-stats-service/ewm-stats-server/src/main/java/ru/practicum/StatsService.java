package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsService {
    private final EndpointHitRepository hitRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public void saveHit(EndpointHitDto dto) {
        EndpointHit hit = EndpointHitMapper.toEntity(dto);
        hit.setRequestTime(LocalDateTime.parse(dto.getRequestTime(), formatter));
        hitRepository.save(hit);
    }


    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {

        LocalDateTime periodStart = LocalDateTime.parse(start, formatter);
        LocalDateTime periodEnd = LocalDateTime.parse(end, formatter);

        if (unique) {
            return hitRepository.findUniqueStats(periodStart, periodEnd, uris);

        } else {
            return hitRepository.findStats(periodStart, periodEnd, uris);
        }
    }
}