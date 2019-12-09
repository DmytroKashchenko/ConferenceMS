package ua.dmytrokashchenko.conferencesms.service.validator;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.service.exceptions.EventValidatorException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
@Component
public class EventValidator implements Validator<Event> {

    @Override
    public void validate(Event event) {
        if (event == null) {
            log.warn("Invalid event");
            throw new EventValidatorException("Invalid event");
        }
        if (event.getAddress() == null) {
            log.warn("Invalid event address");
            throw new EventValidatorException("Invalid event address");
        }
        validateAuthors(event);
        validateTimeEvent(event);
        validatePresentationsTime(event);
    }

    private void validateAuthors(Event event) {
        if (event.getPresentations() == null || event.getPresentations().isEmpty()) {
            return;
        }
        for (Presentation presentation : event.getPresentations()) {
            if (presentation.getAuthor() == null) {
                log.info("Invalid author of the presentation");
                throw new EventValidatorException("Invalid author of the presentation");
            }
            if (presentation.getAuthor().getRole() != Role.SPEAKER) {
                log.info("The author should be the speaker");
                throw new EventValidatorException("The author should be the speaker");
            }
        }
    }

    private void validateTimeEvent(Event event) {
        if (event.getStartDate().isAfter(event.getFinishDate())) {
            log.info("Incorrect event time");
            throw new EventValidatorException("Incorrect event time");
        }
    }

    private void validatePresentationsTime(Event event) {
        if (event.getPresentations() == null || event.getPresentations().size() == 0) {
            return;
        }
        List<Presentation> presentations = event.getPresentationsByStatus(PresentationStatus.CONFIRMED);
        List<Presentation> sortedPresentations = presentations.stream()
                .sorted(Comparator.comparing(Presentation::getStartDate))
                .collect(Collectors.toList());

        LocalDateTime startTimeFirst = sortedPresentations.get(0).getStartDate();
        LocalDateTime endTimeLast = sortedPresentations.get(sortedPresentations.size() - 1)
                .getStartDate().plus(sortedPresentations.get(sortedPresentations.size() - 1).getDuration());

        if (startTimeFirst.isBefore(event.getStartDate()) || endTimeLast.isAfter(event.getFinishDate())) {
            log.info("Invalid presentation time or duration(event time limit)");
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
                log.info("Invalid presentation time or duration");
                throw new EventValidatorException("Invalid presentation time or duration");
            }
        }
    }
}
