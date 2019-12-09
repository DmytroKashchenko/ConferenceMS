package ua.dmytrokashchenko.conferencesms.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ua.dmytrokashchenko.conferencesms.service.validator.AuthorEmail;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresentationForm {
    @NotEmpty
    @Email
    @AuthorEmail
    private String authorEmail;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String presentationTopic;

    @NotEmpty
    @Size(min = 2, max = 1000)
    private String presentationDescription;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate presentationStartDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime presentationStartTime;

    @NotNull
    @Min(30)
    @Max(40)
    private Long duration;

    @NotNull
    private Long eventId;

    @NotEmpty
    private String presentationStatus;

    private Long presentationId;
}
