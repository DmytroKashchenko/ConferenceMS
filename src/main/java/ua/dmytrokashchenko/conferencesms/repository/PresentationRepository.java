package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;

public interface PresentationRepository extends JpaRepository<PresentationEntity, Long> {
}
