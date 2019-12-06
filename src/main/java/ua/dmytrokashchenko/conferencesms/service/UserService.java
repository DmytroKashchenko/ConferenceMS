package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;

import java.util.Map;
import java.util.Set;

public interface UserService extends UserDetailsService {
    void register(User user);

    User login(String email, String password);

    User getById(Long id);

    User getByEmail(String email);

    void update(User user);

    void deleteById(Long id);

    Page<User> getUsers(Pageable pageable);

    Page<User> getUsersByRole(Role role, Pageable pageable);

    Map<User, Double> getSpeakerRatings(Set<User> users);
}
