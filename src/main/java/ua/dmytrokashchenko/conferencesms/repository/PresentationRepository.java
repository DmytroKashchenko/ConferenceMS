package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;

import java.util.List;
import java.util.Set;

@Repository
public interface PresentationRepository extends JpaRepository<PresentationEntity, Long> {
    List<PresentationEntity> findPresentationEntityByAuthorAndStatus(UserEntity author, PresentationStatusEntity status);

    @Query(
            value = "SELECT presentation_presentation_id FROM presentation_registrations pr WHERE pr.user_id = ?",
            nativeQuery = true
    )
    Set<Long> findPresentationsIdsOnUserIsRegistered(Long userId);
}
