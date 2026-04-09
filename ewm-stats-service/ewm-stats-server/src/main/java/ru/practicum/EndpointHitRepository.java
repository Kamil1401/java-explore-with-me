package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {


    @Query("""
              SELECT   new ru.practicum.ViewStats(e.app, e.uri, COUNT(e.id))
                FROM   EndpointHit e
               WHERE   e.requestTime BETWEEN :periodStart AND :periodEnd
                 AND   (:uris IS NULL OR e.uri IN :uris)
            GROUP BY   e.app, e.uri
            """)
    List<ViewStats> findStats(LocalDateTime periodStart, LocalDateTime periodEnd, List<String> uris);


    @Query("""
              SELECT   new ru.practicum.ViewStats(e.app, e.uri, COUNT(DISTINCT e.id))
                FROM   EndpointHit e
               WHERE   e.requestTime BETWEEN :periodStart AND :periodEnd
                 AND   (:uris IS NULL OR e.uri IN :uris)
            GROUP BY   e.app, e.uri
            """)
    List<ViewStats> findUniqueStats(LocalDateTime periodStart, LocalDateTime periodEnd, List<String> uris);
}