package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Message;
import ua.dmytrokashchenko.conferencesms.entity.MessageEntity;

import java.util.stream.Collectors;

@Component
public class MessageMapper {
    private final UserMapper userMapper;

    public MessageMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Message mapEntityToMessage(MessageEntity entity) {
        return Message.builder()
                .id(entity.getId())
                .author(userMapper.mapEntityToUser(entity.getAuthor()))
                .recipients(entity.getRecipients().stream()
                        .map(userMapper::mapEntityToUser)
                        .collect(Collectors.toSet()))
                .subject(entity.getSubject())
                .text(entity.getText())
                .build();
    }

    public MessageEntity mapMessageToEntity(Message message) {
        MessageEntity entity = new MessageEntity();
        entity.setId(message.getId());
        entity.setAuthor(userMapper.mapUserToEntity(message.getAuthor()));
        entity.setRecipients(message.getRecipients().stream()
                .map(userMapper::mapUserToEntity)
                .collect(Collectors.toSet()));
        entity.setSubject(message.getSubject());
        entity.setText(message.getText());
        return entity;
    }
}
