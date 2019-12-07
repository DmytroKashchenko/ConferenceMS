package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.dmytrokashchenko.conferencesms.domain.Message;

public interface EmailSenderService {
    void sendMessages(Message message);

    Page<Message> getMessages(Pageable pageable);
}
