package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Page<EventEntity> findEventEntitiesByStartDateAfter(LocalDateTime startDate, Pageable pageable);
    Page<EventEntity> findEventEntitiesByStartDateBefore(LocalDateTime startDate, Pageable pageable);
}
