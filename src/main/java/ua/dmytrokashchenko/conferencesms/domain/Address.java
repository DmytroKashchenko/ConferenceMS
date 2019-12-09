package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class Address {
    private Long id;

    @NotEmpty
    @Length(max = 255)
    private String name;

    @NotEmpty
    @Length(max = 100)
    private String country;

    @NotEmpty
    @Length(max = 50)
    private String city;

    @NotEmpty
    @Length(max = 255)
    private String detailedAddress;
}
