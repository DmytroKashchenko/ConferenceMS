package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.dmytrokashchenko.conferencesms.entity.BonusEntity;

@Repository
public interface BonusRepository extends JpaRepository<BonusEntity, Long> {
}
