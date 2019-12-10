package ua.dmytrokashchenko.conferencesms.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Message;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.MessageEntity;
import ua.dmytrokashchenko.conferencesms.repository.MessageRepository;
import ua.dmytrokashchenko.conferencesms.service.mapper.MessageMapper;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailSenderServiceImplTest {
    private Message message;
    private MessageEntity messageEntity;
    private User user1;
    private User user2;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private MessageMapper messageMapper;

    @InjectMocks
    @Resource
    private EmailSenderServiceImpl emailSenderService;

    @Before
    public void setUp() {
        User author = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Test")
                .email("test@email.com")
                .password("password")
                .role(Role.MODERATOR)
                .build();
        user1 = User.builder()
                .id(2L)
                .firstName("Test1")
                .lastName("Test1")
                .email("test1@email.com")
                .password("password")
                .role(Role.USER)
                .build();
        user2 = User.builder()
                .id(3L)
                .firstName("Test2")
                .lastName("Test2")
                .email("test2@email.com")
                .password("password")
                .role(Role.USER)
                .build();
        Set<User> users = new TreeSet<>();
        users.add(user1);
        users.add(user2);
        message = new Message(1L, author, users, "subject", "text");
        messageEntity = new MessageEntity();
        messageEntity.setId(1L);
        messageEntity.setSubject("subject");
        messageEntity.setText("text");
    }

    @After
    public void resetMocks() {
        reset(javaMailSender, messageRepository, messageMapper);
    }

    @Test
    public void shouldSuccessfullySendMessages() {
        SimpleMailMessage simpleMailMessage1 = new SimpleMailMessage();
        simpleMailMessage1.setTo(user1.getEmail());
        simpleMailMessage1.setSubject(message.getSubject());
        simpleMailMessage1.setText(message.getText());

        SimpleMailMessage simpleMailMessage2 = new SimpleMailMessage();
        simpleMailMessage2.setTo(user2.getEmail());
        simpleMailMessage2.setSubject(message.getSubject());
        simpleMailMessage2.setText(message.getText());

        doNothing().when(javaMailSender).send(simpleMailMessage1);
        doNothing().when(javaMailSender).send(simpleMailMessage2);

        when(messageMapper.mapMessageToEntity(message)).thenReturn(messageEntity);
        when(messageRepository.save(messageEntity)).thenReturn(messageEntity);

        emailSenderService.sendMessages(message);

        verify(javaMailSender).send(simpleMailMessage1);
        verify(javaMailSender).send(simpleMailMessage2);
        verify(messageRepository).save(messageEntity);
    }

    @Test
    public void shouldSuccessfullyGetMessages() {
        Pageable pageable = Pageable.unpaged();
        Page<MessageEntity> messageEntities = new PageImpl<>(Arrays.asList(messageEntity, messageEntity));

        when(messageMapper.mapEntityToMessage(messageEntity)).thenReturn(message);
        when(messageRepository.findAll(pageable)).thenReturn(messageEntities);

        Page<Message> expected = new PageImpl<>(Arrays.asList(message, message));
        Page<Message> actual = emailSenderService.getMessages(pageable);

        assertEquals(expected, actual);
    }
}