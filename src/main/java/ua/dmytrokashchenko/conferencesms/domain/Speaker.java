package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*@EqualsAndHashCode(callSuper = true)
@Data
@Builder*/
public class Speaker extends User {
    private double rating;

    Speaker(Long id, String firstName, String lastName, String email, String password, Role role) {
        super(id, firstName, lastName, email, password, role);
    }
}
