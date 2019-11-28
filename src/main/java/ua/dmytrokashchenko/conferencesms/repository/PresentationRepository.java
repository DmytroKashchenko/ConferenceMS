package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;

import java.util.List;

public interface PresentationRepository extends JpaRepository<PresentationEntity, Long> {
    List<PresentationEntity> findPresentationEntityByAuthorAndStatus(UserEntity author, PresentationStatusEntity status);
}
