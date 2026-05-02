package ru.practicum.participation_request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("""
            SELECT pr
            FROM   ParticipationRequest pr
            WHERE  pr.event.initiator.id = :userId
              AND  pr.event.id = :eventId
            """)
    List<ParticipationRequest> findByInitiatorIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findByIdInAndEventId(List<Long> ids, Long eventId);

    List<ParticipationRequest> findByEventIdAndStatus(Long eventId, Status status);

    Optional<ParticipationRequest> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);
}
