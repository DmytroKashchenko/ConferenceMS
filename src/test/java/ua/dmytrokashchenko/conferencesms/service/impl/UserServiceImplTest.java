package ua.dmytrokashchenko.conferencesms.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.RoleEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;
import ua.dmytrokashchenko.conferencesms.repository.UserRepository;
import ua.dmytrokashchenko.conferencesms.service.exceptions.UserServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    private User user;
    private UserEntity userEntity;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private UserMapper userMapper;

    @InjectMocks
    @Resource
    private UserServiceImpl userService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Test")
                .email("test@email.com")
                .password("password")
                .role(Role.USER)
                .build();
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Test");
        userEntity.setLastName("Test");
        userEntity.setEmail("test@email.com");
        userEntity.setPassword("password");
        userEntity.setRoleEntity(RoleEntity.USER);
    }

    @After
    public void resetMocks() {
        reset(userRepository, bCryptPasswordEncoder, userMapper);
    }

    @Test
    public void shouldSuccessfullyRegister() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("password");
        when(userMapper.mapUserToEntity(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        userService.register(user);

        verify(userRepository).save(userEntity);
    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyRegistered() {
        exception.expect(UserServiceException.class);
        exception.expectMessage("User with this email is already registered");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEntity));

        userService.register(user);
    }


    @Test
    public void shouldReturnUserById() {
        when(userMapper.mapEntityToUser(userEntity)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        User expected = user;
        User actual = userService.getById(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenNoSuchUser() {
        exception.expect(UserServiceException.class);
        exception.expectMessage("No such user");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        userService.getById(1L);
    }


    @Test
    public void shouldReturnUserByEmail() {
        when(userMapper.mapEntityToUser(userEntity)).thenReturn(user);
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(userEntity));

        User expected = user;
        User actual = userService.getByEmail("test@email.com");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenNoUserWithSuchEmail() {
        exception.expect(UserServiceException.class);
        exception.expectMessage("No user with such email");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        userService.getByEmail("test@email.com");
    }


    @Test
    public void shouldSuccessfullyUpdate() {
        when(userMapper.mapUserToEntity(user)).thenReturn(userEntity);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        userService.update(user);

        verify(userRepository).save(userEntity);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        exception.expect(UserServiceException.class);
        exception.expectMessage("No such user");

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        userService.update(user);
    }

    @Test
    public void shouldReturnUsers() {
        Pageable pageable = Pageable.unpaged();
        Page<UserEntity> userEntities = new PageImpl<>(Arrays.asList(userEntity, userEntity));
        when(userMapper.mapEntityToUser(userEntity)).thenReturn(user);
        when(userRepository.findAll(pageable)).thenReturn(userEntities);

        Page<User> expected = new PageImpl<>(Arrays.asList(user, user));
        Page<User> actual = userService.getUsers(pageable);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnUsersByRole() {
        Pageable pageable = Pageable.unpaged();
        Page<UserEntity> userEntities = new PageImpl<>(Arrays.asList(userEntity, userEntity));

        when(userMapper.mapEntityToUser(userEntity)).thenReturn(user);
        when(userRepository.findByRoleEntity(RoleEntity.USER, pageable)).thenReturn(userEntities);

        Page<User> expected = new PageImpl<>(Arrays.asList(user, user));
        Page<User> actual = userService.getUsersByRole(Role.USER, pageable);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnSpeakerRatings() {
        when(userRepository.findRatingByUserId(1L)).thenReturn(4.0);

        Map<User, Double> expected = new HashMap<>();
        expected.put(user, 4.0);
        Map<User, Double> actual = userService.getSpeakerRatings(Collections.singleton(user));

        assertEquals(expected, actual);
    }

    @Test
    public void isRegisteredByUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEntity));

        boolean actual = userService.isRegistered(user);

        assertTrue(actual);
    }

    @Test
    public void IsRegisteredByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEntity));

        boolean actual = userService.isRegistered(user);

        assertTrue(actual);
    }

    @Test
    public void shouldReturnUserDetailsByEmail() {
        when(userMapper.mapEntityToUser(userEntity)).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEntity));

        UserDetails expected = user;
        UserDetails actual = userService.loadUserByUsername(user.getEmail());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenNoUser() {
        exception.expect(UserServiceException.class);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        userService.loadUserByUsername(user.getEmail());
    }
}