package ua.dmytrokashchenko.conferencesms.service.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.*;
import ua.dmytrokashchenko.conferencesms.service.exceptions.EventValidatorException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventValidatorTest {
    private Validator<Event> eventValidator = new EventValidator();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldSuccessfullyValidate() {
        Event event = initEvent(
                LocalDateTime.of(2020, 1, 1, 0, 5),
                LocalDateTime.of(2020, 1, 1, 9, 20),
                LocalDateTime.of(2020, 1, 1, 0, 6), Duration.ofMinutes(30L),
                LocalDateTime.of(2020, 1, 1, 1, 6), Duration.ofMinutes(30L));
        eventValidator.validate(event);
    }

    @Test
    public void shouldThrowExceptionWhenStartAfterEnd() {
        exception.expect(EventValidatorException.class);
        exception.expectMessage("Incorrect event time");

        Event event = initEvent(
                LocalDateTime.of(2021, 1, 1, 0, 5),
                LocalDateTime.of(2020, 1, 1, 9, 20),
                LocalDateTime.of(2020, 1, 1, 0, 6), Duration.ofMinutes(30L),
                LocalDateTime.of(2020, 1, 1, 1, 6), Duration.ofMinutes(30L));

        eventValidator.validate(event);
    }

    @Test
    public void shouldThrowExceptionWhenPresentationStartsBeforeEventStarts() {
        exception.expect(EventValidatorException.class);
        exception.expectMessage("Invalid presentation time or duration(event time limit)");

        Event event = initEvent(
                LocalDateTime.of(2020, 1, 1, 0, 5),
                LocalDateTime.of(2020, 1, 1, 9, 20),
                LocalDateTime.of(2020, 1, 1, 0, 6), Duration.ofMinutes(30L),
                LocalDateTime.of(2019, 1, 1, 1, 6), Duration.ofMinutes(30L));

        eventValidator.validate(event);
    }

    @Test
    public void shouldThrowExceptionWhenPresentationStartAfterEventEnds() {
        exception.expect(EventValidatorException.class);
        exception.expectMessage("Invalid presentation time or duration(event time limit)");

        Event event = initEvent(
                LocalDateTime.of(2020, 1, 1, 0, 5),
                LocalDateTime.of(2020, 1, 1, 9, 20),
                LocalDateTime.of(2020, 1, 1, 0, 6), Duration.ofMinutes(30L),
                LocalDateTime.of(2021, 1, 1, 1, 6), Duration.ofMinutes(30L));

        eventValidator.validate(event);
    }

    @Test
    public void shouldThrowExceptionWhenPresentationEndsAfterEventEnds() {
        exception.expect(EventValidatorException.class);
        exception.expectMessage("Invalid presentation time or duration(event time limit)");

        Event event = initEvent(
                LocalDateTime.of(2020, 1, 1, 0, 5),
                LocalDateTime.of(2020, 1, 1, 9, 20),
                LocalDateTime.of(2020, 1, 1, 0, 6), Duration.ofMinutes(30L),
                LocalDateTime.of(2020, 1, 1, 1, 6), Duration.ofMinutes(30000L));

        eventValidator.validate(event);
    }

    @Test
    public void shouldThrowExceptionWhenPreviousPresentationEndsAfterNextPresentationStarts() {
        exception.expect(EventValidatorException.class);
        exception.expectMessage("Invalid presentation time or duration");

        Event event = initEvent(
                LocalDateTime.of(2020, 1, 1, 0, 5),
                LocalDateTime.of(2020, 1, 1, 9, 20),
                LocalDateTime.of(2020, 1, 1, 0, 6), Duration.ofMinutes(240L),
                LocalDateTime.of(2020, 1, 1, 1, 6), Duration.ofMinutes(30L));

        eventValidator.validate(event);
    }


    private Event initEvent(LocalDateTime start, LocalDateTime finish,
                            LocalDateTime presentation1Start, Duration presentation1Duration,
                            LocalDateTime presentation2Start, Duration presentation2Duration) {
        User author = User.builder()
                .id(42L)
                .firstName("First")
                .lastName("Last")
                .role(Role.SPEAKER)
                .email("email@example.com")
                .password("Password")
                .build();

        Presentation presentation1 = Presentation.builder()
                .id(99L)
                .author(author)
                .topic("Presentation 1 topic")
                .description("Presentation 1 description")
                .startDate(presentation1Start)
                .duration(presentation1Duration)
                .status(PresentationStatus.CONFIRMED)
                .build();

        Presentation presentation2 = Presentation.builder()
                .id(98L)
                .author(author)
                .topic("Presentation 2 topic")
                .description("Presentation 2 description")
                .startDate(presentation2Start)
                .duration(presentation2Duration)
                .status(PresentationStatus.CONFIRMED)
                .build();

        List<Presentation> presentations = new ArrayList<>();
        presentations.add(presentation1);
        presentations.add(presentation2);

        Address address = Address.builder()
                .city("CityName")
                .name("AddressName")
                .country("Country")
                .detailedAddress("Address details")
                .id(100L)
                .build();

        return Event.builder()
                .id(100L)
                .name("EventName")
                .eventDetails("Some details of the event")
                .startDate(start)
                .finishDate(finish)
                .address(address)
                .presentations(presentations)
                .build();
    }
}