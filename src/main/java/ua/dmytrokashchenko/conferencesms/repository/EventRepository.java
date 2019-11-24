package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
