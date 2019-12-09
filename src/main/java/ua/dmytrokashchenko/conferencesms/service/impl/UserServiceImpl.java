package ua.dmytrokashchenko.conferencesms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.RoleEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;
import ua.dmytrokashchenko.conferencesms.repository.UserRepository;
import ua.dmytrokashchenko.conferencesms.service.UserService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.UserServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    @Override
    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.info("User with this email is already registered");
            throw new UserServiceException("User with this email is already registered");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(userMapper.mapUserToEntity(user));
    }

    @Override
    public User getById(Long id) {
        if (id <= 0) {
            log.warn("Invalid id");
            throw new UserServiceException("Invalid id");
        }
        return userMapper.mapEntityToUser(userRepository.findById(id)
                .orElseThrow(() -> new UserServiceException("No such user")));
    }

    @Override
    public User getByEmail(String email) {
        if (email == null || email.isEmpty()) {
            log.warn("Invalid email");
            throw new UserServiceException("Invalid email");
        }
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserServiceException("Wrong email"));
        return userMapper.mapEntityToUser(entity);
    }

    @Override
    public void update(User user) {
        if (user == null) {
            log.warn("Invalid user");
            throw new UserServiceException("Invalid user");
        }
        userRepository.save(userMapper.mapUserToEntity(user));
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::mapEntityToUser);
    }

    @Override
    public Page<User> getUsersByRole(Role role, Pageable pageable) {
        return userRepository.findByRoleEntity(RoleEntity.valueOf(role.name()), pageable)
                .map(userMapper::mapEntityToUser);
    }

    @Override
    public Map<User, Double> getSpeakerRatings(Set<User> users) {
        if (users == null) {
            log.warn("Invalid users");
            throw new UserServiceException("Invalid users");
        }
        Map<User, Double> ratings = new HashMap<>();
        users.forEach((x) -> ratings.put(x, userRepository.findRatingByUserId(x.getId())));
        return ratings;
    }

    @Override
    public boolean isRegistered(User user) {
        if (user == null) {
            log.warn("Invalid user");
            throw new UserServiceException("Invalid user");
        }
        return userRepository.findByEmail(user.getEmail()).isPresent();
    }

    @Override
    public boolean isRegistered(String email) {
        if (email == null || email.isEmpty()) {
            log.warn("Invalid email");
            throw new UserServiceException("Invalid email");
        }
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.mapEntityToUser(userRepository.findByEmail(username)
                .orElseThrow(UserServiceException::new));
    }
}
