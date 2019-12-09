package ua.dmytrokashchenko.conferencesms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Message;
import ua.dmytrokashchenko.conferencesms.repository.MessageRepository;
import ua.dmytrokashchenko.conferencesms.service.EmailSenderService;
import ua.dmytrokashchenko.conferencesms.service.mapper.MessageMapper;

@Log4j
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    private void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void sendMessages(Message message) {
        message.getRecipients()
                .forEach((user) -> this.sendMessage(user.getEmail(), message.getSubject(), message.getText()));
        log.info("Message successfully sent.\nAuthor:" + message.getAuthor().getFirstName() + " " +
                message.getAuthor().getLastName() +
                "\nSubject: " + message.getSubject() + "\nText: " + message.getText());
        messageRepository.save(messageMapper.mapMessageToEntity(message));
    }

    @Override
    public Page<Message> getMessages(Pageable pageable) {
        if (pageable == null) {
            log.warn("Invalid pageable object");
            throw new IllegalArgumentException("Invalid pageable object");
        }
        return messageRepository.findAll(pageable).map(messageMapper::mapEntityToMessage);
    }
}
