package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsService {
    private final EndpointHitRepository hitRepository;


    public void saveHit(EndpointHitDto dto) {
        EndpointHit hit = EndpointHitMapper.toEntity(dto);
        hitRepository.save(hit);
    }


    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start не может быть позже end");
        }

        if (unique) {
            return hitRepository.findUniqueStats(start, end, uris);

        } else {
            return hitRepository.findStats(start, end, uris);
        }
    }
}