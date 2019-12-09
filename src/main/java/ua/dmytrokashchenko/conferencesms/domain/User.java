package ua.dmytrokashchenko.conferencesms.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails, Comparable<User> {
    private Long id;

    @NotEmpty(message = "*Please provide your name")
    @Size(min = 2, max = 50)
    @Pattern(regexp = "([A-Z]([a-z]+\\s?)){1,3}")
    private String firstName;

    @NotEmpty(message = "*Please provide your last name")
    @Size(min = 2, max = 50)
    @Pattern(regexp = "([A-Z]([a-z]+\\s?)){1,3}")
    private String lastName;

    @NotEmpty(message = "*Please provide an email")
    @Length(max = 254)
    @Email(message = "*Please provide a valid Email")
    private String email;

    @Length(min = 6, max = 255, message = "*Your password must have at least 6 characters")
    @NotEmpty(message = "*Please provide your password")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}")
    private String password;

    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int compareTo(User o) {
        return Comparator.comparing(User::getFirstName)
                .thenComparing(User::getLastName)
                .thenComparing(User::getEmail)
                .compare(this, o);
    }
}
