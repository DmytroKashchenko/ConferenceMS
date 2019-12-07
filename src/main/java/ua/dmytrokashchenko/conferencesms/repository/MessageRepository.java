package ua.dmytrokashchenko.conferencesms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dmytrokashchenko.conferencesms.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
