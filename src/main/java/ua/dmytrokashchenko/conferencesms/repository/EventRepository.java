package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Page<EventEntity> findEventEntitiesByStartDateAfter(LocalDateTime startDate, Pageable pageable);

    Page<EventEntity> findEventEntitiesByStartDateBefore(LocalDateTime startDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM events e WHERE e.event_id IN " +
                    "(SELECT event_id FROM event_presentations ep WHERE ep.presentation_id = ?1)",
            nativeQuery = true
    )
    Optional<EventEntity> findEventEntityByPresentationEntityId(Long id);
}
