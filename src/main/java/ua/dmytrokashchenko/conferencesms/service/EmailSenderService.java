package ua.dmytrokashchenko.conferencesms.service;

import java.util.Set;

public interface EmailSenderService {
    void sendMessage(String to, String subject, String message);
    void sendMessages(Set<String> emails, String subject, String text);
}
