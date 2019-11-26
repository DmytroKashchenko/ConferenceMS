package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public void addPresentation(Presentation presentation) {
        if (presentations == null) {
            presentations = new ArrayList<>();
        }
        presentations.add(presentation);
    }
}
