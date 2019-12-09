package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class Event {
    private Long id;

    @NotEmpty
    @Length(max = 255)
    private String name;

    @NotEmpty
    @Length(max = 2000)
    private String eventDetails;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime finishDate;

    @NotNull
    private Address address;

    private List<Presentation> presentations;

    public void addPresentation(Presentation presentation) {
        if (presentations == null) {
            presentations = new ArrayList<>();
        }
        presentations.add(presentation);
    }

    public List<Presentation> getPresentationsByStatus(PresentationStatus status) {
        if (this.presentations == null) {
            return Collections.emptyList();
        }
        List<Presentation> presentations = new ArrayList<>(this.presentations);
        List<Presentation> unconfirmedPresentations = new ArrayList<>();
        for (Presentation p : presentations) {
            if (p.getStatus() != status) {
                unconfirmedPresentations.add(p);
            }
        }
        presentations.removeAll(unconfirmedPresentations);
        return presentations;
    }

    public Presentation getPresentationById(Long presentationId) {
        if (presentations == null || presentationId == null) {
            return null;
        }
        for (Presentation p : presentations) {
            if (p.getId().equals(presentationId)) {
                return p;
            }
        }
        return null;
    }
}
