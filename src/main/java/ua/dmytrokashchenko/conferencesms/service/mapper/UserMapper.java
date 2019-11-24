package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.RoleEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;

@Component
public class UserMapper {

    public User mapEntityToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .role(Role.valueOf(userEntity.getRoleEntity().name()))
                .build();
    }

    public UserEntity mapUserToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setRoleEntity(RoleEntity.valueOf(user.getRole().name()));
        return userEntity;
    }
}
