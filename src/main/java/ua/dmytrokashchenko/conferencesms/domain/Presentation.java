package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class Presentation {
    private Long id;
    private User author;
    private String topic;
    private String description;
    private LocalDateTime startDate;
    private Duration duration;
    private PresentationStatus status;
}
