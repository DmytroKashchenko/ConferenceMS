package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.entity.AddressEntity;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventMapperTest {
    @MockBean
    private AddressMapper addressMapper;

    @MockBean
    private PresentationMapper presentationMapper;

    @InjectMocks
    @Resource
    private EventMapper eventMapper;

    private Event event;
    private EventEntity eventEntity;

    private Address address;
    private AddressEntity addressEntity;

    private Presentation presentation1;
    private Presentation presentation2;
    private PresentationEntity presentationEntity1;
    private PresentationEntity presentationEntity2;

    @Before
    public void setUp() {
        address = Address.builder()
                .id(100L)
                .build();

        addressEntity = new AddressEntity();
        addressEntity.setId(100L);

        presentation1 = Presentation.builder()
                .id(99L)
                .build();

        presentation2 = Presentation.builder()
                .id(98L)
                .build();

        presentationEntity1 = new PresentationEntity();
        presentationEntity1.setId(99L);

        presentationEntity2 = new PresentationEntity();
        presentationEntity2.setId(98L);

        event = Event.builder()
                .id(100L)
                .name("EventName")
                .eventDetails("Some details of the event")
                .startDate(LocalDateTime.of(2020, 1, 1, 0, 5))
                .finishDate(LocalDateTime.of(2020, 1, 1, 9, 20))
                .address(address)
                .presentations(Arrays.asList(presentation1, presentation2))
                .build();

        eventEntity = new EventEntity();
        eventEntity.setId(100L);
        eventEntity.setName("EventName");
        eventEntity.setEventDetails("Some details of the event");
        eventEntity.setStartDate(LocalDateTime.of(2020, 1, 1, 0, 5));
        eventEntity.setFinishDate(LocalDateTime.of(2020, 1, 1, 9, 20));
        eventEntity.setAddressEntity(addressEntity);
        eventEntity.setPresentationEntities(Arrays.asList(presentationEntity1, presentationEntity2));
    }


    @Test
    public void shouldSuccessfullyMapEntityToEvent() {
        when(addressMapper.mapEntityToAddress(addressEntity)).thenReturn(address);
        when(presentationMapper.mapEntityToPresentation(presentationEntity1)).thenReturn(presentation1);
        when(presentationMapper.mapEntityToPresentation(presentationEntity2)).thenReturn(presentation2);

        Event actual = eventMapper.mapEntityToEvent(eventEntity);
        Event expected = event;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSuccessfullyMapEventToEntity() {
        when(addressMapper.mapAddressToEntity(address)).thenReturn(addressEntity);
        when(presentationMapper.mapPresentationToEntity(presentation1)).thenReturn(presentationEntity1);
        when(presentationMapper.mapPresentationToEntity(presentation2)).thenReturn(presentationEntity2);

        EventEntity actual = eventMapper.mapEventToEntity(event);
        EventEntity expected = eventEntity;

        assertEquals(expected, actual);
    }
}
