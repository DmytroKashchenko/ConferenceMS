package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private Long id;
    private String name;
    private String country;
    private String city;
    private String detailedAddress;
}
