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
import java.util.Optional;

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
    public void resetMock() {
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
    }


    @Test
    public void getById() {
    }

    @Test
    public void getByEmail() {
    }

    @Test
    public void update() {
    }

    @Test
    public void getUsers() {
    }

    @Test
    public void getUsersByRole() {
    }

    @Test
    public void getSpeakerRatings() {
    }

    @Test
    public void isRegistered() {
    }

    @Test
    public void testIsRegistered() {
    }

    @Test
    public void loadUserByUsername() {
    }
}