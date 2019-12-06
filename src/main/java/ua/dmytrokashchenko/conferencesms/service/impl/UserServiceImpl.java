package ua.dmytrokashchenko.conferencesms.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import ua.dmytrokashchenko.conferencesms.service.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final Validator<User> validator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Validator<User> validator,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public void register(User user) {
        validator.validate(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            LOGGER.info("User with this email is already registered");
            throw new UserServiceException("User with this email is already registered");
        }
        userRepository.save(userMapper.mapUserToEntity(user));
    }

    @Override
    public User login(String email, String password) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserServiceException("Can't find user with such email or password"));
        boolean correctPassword = bCryptPasswordEncoder.matches(password, userEntity.getPassword());
        if (!correctPassword) {
            LOGGER.warn("Can't find user with such email or password");
            throw new UserServiceException("Can't find user with such email or password");
        }
        return userMapper.mapEntityToUser(userEntity);
    }

    @Override
    public User getById(Long id) {
        if (id <= 0) {
            LOGGER.warn("Invalid id");
            throw new UserServiceException("Invalid id");
        }
        return userMapper.mapEntityToUser(userRepository.findById(id).orElseThrow(() ->
                new UserServiceException("Empty user")));
    }

    @Override
    public User getByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email).orElseThrow(() -> new UserServiceException("Wrong email"));
        return userMapper.mapEntityToUser(entity);
    }

    @Override
    public void update(User user) {
        if (user == null) {
            LOGGER.warn("Invalid user");
            throw new UserServiceException("Invalid user");
        }
        userRepository.save(userMapper.mapUserToEntity(user));
    }

    @Override
    public void deleteById(Long id) {
        if (id <= 0 || !userRepository.findById(id).isPresent()) {
            LOGGER.warn("Invalid id");
            throw new UserServiceException("Invalid id");
        }
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        if (pageable == null) {
            LOGGER.warn("Invalid page");
            throw new UserServiceException("Invalid page");
        }
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
            LOGGER.warn("Invalid users");
            throw new UserServiceException("Invalid users");
        }
        Map<User, Double> ratings = new HashMap<>();
        users.forEach((x) -> ratings.put(x, userRepository.findRatingByUserId(x.getId())));
        return ratings;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.mapEntityToUser(userRepository.findByEmail(username)
                .orElseThrow(UserServiceException::new));
    }
}
