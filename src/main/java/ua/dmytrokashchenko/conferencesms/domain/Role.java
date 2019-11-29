package ua.dmytrokashchenko.conferencesms.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MODERATOR,
    SPEAKER,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
