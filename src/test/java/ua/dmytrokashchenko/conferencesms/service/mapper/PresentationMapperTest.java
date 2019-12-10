package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PresentationMapperTest {
    @MockBean
    private UserMapper userMapper;

    @InjectMocks
    @Resource
    private PresentationMapper presentationMapper;

    private User author;
    private UserEntity authorEntity;
    private Presentation presentation;
    private PresentationEntity presentationEntity;

    @Before
    public void setUp() {
        author = User.builder()
                .id(42L)
                .build();

        authorEntity = new UserEntity();
        authorEntity.setId(42L);

        presentation = Presentation.builder()
                .id(99L)
                .author(author)
                .topic("Presentation topic")
                .description("Presentation description")
                .startDate(LocalDateTime.of(2020, 1, 1, 0, 6))
                .duration(Duration.ofMinutes(30L))
                .status(PresentationStatus.CONFIRMED)
                .registrations(Collections.emptyMap())
                .ratings(Collections.emptyMap())
                .build();

        presentationEntity = new PresentationEntity();
        presentationEntity.setId(99L);
        presentationEntity.setAuthor(authorEntity);
        presentationEntity.setTopic("Presentation topic");
        presentationEntity.setDescription("Presentation description");
        presentationEntity.setStartDate(LocalDateTime.of(2020, 1, 1, 0, 6));
        presentationEntity.setDuration(Duration.ofMinutes(30L));
        presentationEntity.setStatus(PresentationStatusEntity.CONFIRMED);
        presentationEntity.setRegistrations(Collections.emptyMap());
        presentationEntity.setRatings(Collections.emptyMap());
    }

    @Test
    public void shouldSuccessfullyMapEntityToPresentation() {
        when(userMapper.mapEntityToUser(authorEntity)).thenReturn(author);

        Presentation actual = presentationMapper.mapEntityToPresentation(presentationEntity);
        Presentation expected = presentation;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSuccessfullyMapPresentationToEntity() {
        when(userMapper.mapUserToEntity(author)).thenReturn(authorEntity);

        PresentationEntity actual = presentationMapper.mapPresentationToEntity(presentation);
        PresentationEntity expected = presentationEntity;

        assertEquals(expected, actual);
    }
}
