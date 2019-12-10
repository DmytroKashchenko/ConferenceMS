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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;
import ua.dmytrokashchenko.conferencesms.repository.EventRepository;
import ua.dmytrokashchenko.conferencesms.service.exceptions.EventServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.EventMapper;
import ua.dmytrokashchenko.conferencesms.service.validator.Validator;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceImplTest {
    private Event event;
    private EventEntity eventEntity;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private EventMapper eventMapper;

    @MockBean
    private Validator<Event> eventValidator;

    @InjectMocks
    @Resource
    EventServiceImpl eventService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        event = Event.builder()
                .id(1L)
                .build();
        eventEntity = new EventEntity();
        eventEntity.setId(1L);
    }

    @After
    public void resetMocks() {
        reset(eventRepository, eventValidator, eventMapper);
    }

    @Test
    public void shouldSuccessfullySaveEvent() {
        doNothing().when(eventValidator).validate(event);
        when(eventMapper.mapEventToEntity(event)).thenReturn(eventEntity);
        when(eventRepository.save(eventEntity)).thenReturn(eventEntity);

        eventService.save(event);

        verify(eventRepository).save(eventEntity);
        verify(eventValidator).validate(event);
    }

    @Test
    public void shouldSuccessfullyGetById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
        when(eventMapper.mapEntityToEvent(eventEntity)).thenReturn(event);

        Event expected = event;
        Event actual = eventService.getById(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenEventNotFounded() {
        exception.expect(EventServiceException.class);
        exception.expectMessage("No event with this id");

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        eventService.getById(1L);
    }

    @Test
    public void shouldSuccessfullyReturnEventByPresentationId() {
        when(eventRepository.findEventEntityByPresentationEntityId(1L)).thenReturn(Optional.of(eventEntity));
        when(eventMapper.mapEntityToEvent(eventEntity)).thenReturn(event);

        Event expected = event;
        Event actual = eventService.getEventByPresentationId(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenNoEventWithPresentationId() {
        exception.expect(EventServiceException.class);
        exception.expectMessage("No event with this presentation");

        when(eventRepository.findEventEntityByPresentationEntityId(1L)).thenReturn(Optional.empty());

        eventService.getEventByPresentationId(1L);
    }

    @Test
    public void shouldReturnUpcomingEvents() {
        Pageable pageable = Pageable.unpaged();
        Page<EventEntity> eventEntities = new PageImpl<>(Arrays.asList(eventEntity, eventEntity));

        when(eventRepository.findEventEntitiesByFinishDateAfter(any(), eq(pageable)))
                .thenReturn(eventEntities);
        when(eventMapper.mapEntityToEvent(eventEntity)).thenReturn(event);

        Page<Event> expected = new PageImpl<>(Arrays.asList(event, event));
        Page<Event> actual = eventService.getUpcomingEvents(pageable);

        assertEquals(expected, actual);
    }

    @Test
    public void getPastEvents() {
        Pageable pageable = Pageable.unpaged();
        Page<EventEntity> eventEntities = new PageImpl<>(Arrays.asList(eventEntity, eventEntity));

        when(eventRepository.findEventEntitiesByStartDateBefore(any(), eq(pageable)))
                .thenReturn(eventEntities);
        when(eventMapper.mapEntityToEvent(eventEntity)).thenReturn(event);

        Page<Event> expected = new PageImpl<>(Arrays.asList(event, event));
        Page<Event> actual = eventService.getPastEvents(pageable);

        assertEquals(expected, actual);
    }
}