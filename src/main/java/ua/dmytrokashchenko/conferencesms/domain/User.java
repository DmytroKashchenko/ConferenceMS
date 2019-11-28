package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Collections;

@Data
@Builder
public class User implements UserDetails {
    private Long id;

    @NotEmpty(message = "*Please provide your name")
    private String firstName;

    @NotEmpty(message = "*Please provide your last name")
    private String lastName;

    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Length(min = 6, message = "*Your password must have at least 6 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;

    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getUsername() {
        return email;
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
}
