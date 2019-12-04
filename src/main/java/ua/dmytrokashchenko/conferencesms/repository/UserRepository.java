package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.dmytrokashchenko.conferencesms.domain.Speaker;
import ua.dmytrokashchenko.conferencesms.entity.RoleEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;

import javax.persistence.*;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByRoleEntity(RoleEntity roleEntity, Pageable pageable);

    @Query(
            value = "SELECT AVG(presentation_rating) FROM " +
                    "(SELECT presentations.user_id, presentations.presentation_id, AVG(rating) AS presentation_rating " +
                    "FROM presentations " +
                    "JOIN user_ratings ON presentation_presentation_id = presentation_id GROUP BY presentation_id) AS a " +
                    "WHERE user_id = ? GROUP BY user_id",
            nativeQuery = true
    )
    Double findRatingByUserId(Long userId);
}
