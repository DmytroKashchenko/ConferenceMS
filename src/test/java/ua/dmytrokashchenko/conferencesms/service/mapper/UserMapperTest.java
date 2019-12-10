package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.RoleEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    private User user;
    private UserEntity userEntity;
    private UserMapper userMapper = new UserMapper();

    @Before
    public void setUp() {
        user = User.builder()
                .firstName("First")
                .lastName("Last")
                .email("email@email.com")
                .password("password")
                .role(Role.USER)
                .id(42L)
                .build();

        userEntity = new UserEntity();
        userEntity.setFirstName("First");
        userEntity.setLastName("Last");
        userEntity.setEmail("email@email.com");
        userEntity.setPassword("password");
        userEntity.setRoleEntity(RoleEntity.USER);
        userEntity.setId(42L);
    }

    @Test
    public void shouldSuccessfullyMapUserToeEntity() {
        UserEntity entity = userMapper.mapUserToEntity(user);

        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getFirstName(), entity.getFirstName());
        assertEquals(user.getLastName(), entity.getLastName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertEquals(user.getPassword(), entity.getPassword());
        assertEquals(user.getRole().name(), userEntity.getRoleEntity().name());
    }

    @Test
    public void shouldSuccessfullyMapEntityToUser() {
        User user1 = userMapper.mapEntityToUser(userEntity);

        assertEquals(userEntity.getId(), user1.getId());
        assertEquals(userEntity.getFirstName(), user1.getFirstName());
        assertEquals(userEntity.getLastName(), user1.getLastName());
        assertEquals(userEntity.getEmail(), user1.getEmail());
        assertEquals(userEntity.getRoleEntity().name(), user1.getRole().name());
    }
}
