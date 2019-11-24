package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.data.domain.Page;
import ua.dmytrokashchenko.conferencesms.domain.User;

public interface UserService {
    User register(User user);

    User login(String email, String password);

    User getById(Long id);

    User getByEmail(String email);

    void update(User user);

    void deleteById(Long id);

    Page<User> getUsers(Integer pageNo, Integer pageSize, String sortBy);

    void changeUserRole(User user);
}
