package ua.dmytrokashchenko.conferencesms.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.*;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;
import ua.dmytrokashchenko.conferencesms.entity.RoleEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;
import ua.dmytrokashchenko.conferencesms.repository.PresentationRepository;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.UserService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.PresentationServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.PresentationMapper;
import ua.dmytrokashchenko.conferencesms.service.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PresentationServiceImplTest {
    private Presentation presentation;
    private PresentationEntity presentationEntity;
    private User user;
    private UserEntity userEntity;
    private Event event;

    @MockBean
    private PresentationRepository presentationRepository;

    @MockBean
    private PresentationMapper presentationMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private UserService userService;

    @InjectMocks
    @Resource
    private PresentationServiceImpl presentationService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        presentation = Presentation.builder()
                .id(42L)
                .build();
        presentationEntity = new PresentationEntity();
        presentationEntity.setId(1L);
        user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Test")
                .email("test@email.com")
                .password("password")
                .role(Role.USER)
                .build();
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Test");
        userEntity.setLastName("Test");
        userEntity.setEmail("test@email.com");
        userEntity.setPassword("password");
        userEntity.setRoleEntity(RoleEntity.USER);
        event = Event.builder()
                .id(13L)
                .presentations(Collections.singletonList(presentation))
                .build();
    }

    @After
    public void resetMocks() {
        reset(presentationRepository, presentationMapper, userMapper, eventService, userService);
    }

    @Test
    public void shouldReturnPresentationsByAuthorAndStatus() {
        when(userMapper.mapUserToEntity(user)).thenReturn(userEntity);
        when(presentationRepository
                .findPresentationEntityByAuthorAndStatus(userEntity, PresentationStatusEntity.CONFIRMED))
                .thenReturn(Arrays.asList(presentationEntity, presentationEntity));
        when(presentationMapper.mapEntityToPresentation(presentationEntity)).thenReturn(presentation);

        List<Presentation> expected = Arrays.asList(presentation, presentation);
        List<Presentation> actual = presentationService
                .getPresentationsByAuthorAndStatus(user, PresentationStatus.CONFIRMED);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnPresentationsIdsOnUserIsRegistered() {
        Set<Long> ids = new HashSet<>();
        ids.add(42L);
        ids.add(24L);
        when(presentationRepository.findPresentationsIdsOnUserIsRegistered(1L)).thenReturn(ids);
        Set<Long> expected = new HashSet<>();
        expected.add(42L);
        expected.add(24L);
        Set<Long> actual = presentationService.getPresentationsIdsOnUserIsRegistered(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSuccessfullyRegisterForPresentation() {
        when(eventService.getEventByPresentationId(42L)).thenReturn(event);
        doNothing().when(eventService).save(event);

        presentationService.registerForPresentation(42L, user);

        verify(eventService).save(event);
    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyRegistered() {
        exception.expect(PresentationServiceException.class);
        exception.expectMessage("User is already registered for the presentation");

        Map<User, Boolean> registrations = new HashMap<>();
        registrations.put(user, false);
        presentation.setRegistrations(registrations);
        when(eventService.getEventByPresentationId(42L)).thenReturn(event);
        presentationService.registerForPresentation(42L, user);
    }

    @Test
    public void shouldSuccessfullyRatePresentation() {
        when(eventService.getEventByPresentationId(42L)).thenReturn(event);
        doNothing().when(eventService).save(event);
        presentation.getRegistrations().put(user, true);

        presentationService.ratePresentation(user, 4, 42L);

        verify(eventService).save(event);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotVisitor() {
        exception.expect(PresentationServiceException.class);
        exception.expectMessage("User isn't a visitor");

        when(eventService.getEventByPresentationId(42L)).thenReturn(event);
        presentation.getRegistrations().put(user, false);

        presentationService.ratePresentation(user, 4, 42L);
    }

    @Test
    public void shouldSuccessfullyRegisterVisitor() {
        Map<User, Boolean> registrations = new HashMap<>();
        registrations.put(user, false);
        presentation.setRegistrations(registrations);
        when(eventService.getEventByPresentationId(42L)).thenReturn(event);
        when(userService.getById(1L)).thenReturn(user);
        doNothing().when(eventService).save(event);

        presentationService.registerVisitor(42L, 1L);

        verify(eventService).save(event);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotRegistered() {
        exception.expect(PresentationServiceException.class);
        exception.expectMessage("User not registered");

        when(eventService.getEventByPresentationId(42L)).thenReturn(event);
        when(userService.getById(1L)).thenReturn(user);

        presentationService.registerVisitor(42L, 1L);
    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyVisitor() {
        exception.expect(PresentationServiceException.class);
        exception.expectMessage("User is already a visitor");

        Map<User, Boolean> registrations = new HashMap<>();
        registrations.put(user, true);
        presentation.setRegistrations(registrations);
        when(eventService.getEventByPresentationId(42L)).thenReturn(event);
        when(userService.getById(1L)).thenReturn(user);

        presentationService.registerVisitor(42L, 1L);
    }
}