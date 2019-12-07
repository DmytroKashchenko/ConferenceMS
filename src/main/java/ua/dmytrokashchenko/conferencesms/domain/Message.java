package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class Message {
    private Long id;

    @NotEmpty(message = "Author should exist")
    private User author;

    @NotNull
    private Set<User> recipients;

    @NotEmpty(message = "*Please provide subject")
    private String subject;

    @NotEmpty(message = "*Please provide message")
    private String text;
}
