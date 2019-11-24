package ua.dmytrokashchenko.conferencesms.service.validator;

import org.apache.log4j.Logger;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.service.exceptions.EventValidatorException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventValidator implements Validator<Event> {
    private static final Logger LOGGER = Logger.getLogger(EventValidator.class);

    @Override
    public void validate(Event event) {
        if (event == null) {
            LOGGER.warn("Invalid event");
            throw new EventValidatorException("Invalid event");
        }
        if (event.getAddress() == null) {
            LOGGER.warn("Invalid event address");
            throw new EventValidatorException("Invalid event address");
        }
        validateAuthors(event);
        validateTimeEvent(event);
        validatePresentationsTime(event);
    }

    private void validateAuthors(Event event) {
        for (Presentation presentation : event.getPresentations()) {
            if (presentation.getAuthor() == null) {
                LOGGER.warn("Invalid author of the presentation");
                throw new EventValidatorException("Invalid author of the presentation");
            }
            if (presentation.getAuthor().getRole() != Role.SPEAKER) {
                LOGGER.warn("The author should be the speaker");
                throw new EventValidatorException("The author should be the speaker");
            }
        }
    }

    private void validateTimeEvent(Event event) {
        if (event.getStartDate().isAfter(event.getFinishDate())) {
            LOGGER.warn("Incorrect event time");
            throw new EventValidatorException("Incorrect event time");
        }
    }

    private void validatePresentationsTime(Event event) {
        List<Presentation> presentations = event.getPresentations();
        if (presentations == null || presentations.size() == 0) {
            return;
        }
        List<Presentation> sortedPresentations = presentations.stream()
                .sorted(Comparator.comparing(Presentation::getStartDate))
                .collect(Collectors.toList());

        LocalDateTime startTimeFirst = sortedPresentations.get(0).getStartDate();
        LocalDateTime endTimeLast = sortedPresentations.get(sortedPresentations.size() - 1)
                .getStartDate().plus(sortedPresentations.get(sortedPresentations.size() - 1).getDuration());

        if (startTimeFirst.isBefore(event.getStartDate()) || endTimeLast.isAfter(event.getFinishDate())) {
            LOGGER.warn("Invalid presentation time or duration(event time limit)");
            throw new EventValidatorException("Invalid presentation time or duration(event time limit)");
        }
        if (sortedPresentations.size() == 1) {
            return;
        }
        for (int i = 1; i < sortedPresentations.size(); i++) {
            LocalDateTime previousPresentationEndTime = sortedPresentations.get(i - 1).getStartDate()
                    .plus(sortedPresentations.get(i - 1).getDuration());
            LocalDateTime currentPresentationStartTime = sortedPresentations.get(i).getStartDate();
            if (previousPresentationEndTime.isAfter(currentPresentationStartTime)) {
                LOGGER.warn("Invalid presentation time or duration");
                throw new EventValidatorException("Invalid presentation time or duration");
            }
        }
    }
}
