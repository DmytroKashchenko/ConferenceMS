package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Event {
    private Long id;
    private String name;
    private String eventDetails;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Address address;
    private List<Presentation> presentations;
}
