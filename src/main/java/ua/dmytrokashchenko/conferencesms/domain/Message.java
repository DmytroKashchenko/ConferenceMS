package ua.dmytrokashchenko.conferencesms.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Message {
    private Long id;

    @NotEmpty(message = "Author should exist")
    private User author;

    @NotNull
    private Set<User> recipients;

    @NotEmpty(message = "*Please provide subject")
    @Length(max = 255)
    private String subject;

    @NotEmpty(message = "*Please provide message")
    @Length(max = 10000)
    private String text;
}
