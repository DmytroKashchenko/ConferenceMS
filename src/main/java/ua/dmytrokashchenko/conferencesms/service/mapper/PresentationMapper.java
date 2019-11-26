package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class PresentationMapper {
    private final UserMapper userMapper;

    public PresentationMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Presentation mapEntityToPresentation(PresentationEntity presentationEntity) {
        return Presentation.builder()
                .id(presentationEntity.getId())
                .topic(presentationEntity.getTopic())
                .description(presentationEntity.getDescription())
                .startDate(presentationEntity.getStartDate())
                .duration(presentationEntity.getDuration())
                .author(userMapper.mapEntityToUser(presentationEntity.getAuthor()))
                .status(PresentationStatus.valueOf(presentationEntity.getStatus().name()))
                .ratings(remapUserEntityMap(presentationEntity.getRatings()))
                .registrations(remapUserEntityMap(presentationEntity.getRegistrations()))
                .build();
    }

    public PresentationEntity mapPresentationToEntity(Presentation presentation) {
        PresentationEntity presentationEntity = new PresentationEntity();
        presentationEntity.setId(presentation.getId());
        presentationEntity.setTopic(presentation.getTopic());
        presentationEntity.setDescription(presentation.getDescription());
        presentationEntity.setAuthor(userMapper.mapUserToEntity(presentation.getAuthor()));
        presentationEntity.setStartDate(presentation.getStartDate());
        presentationEntity.setDuration(presentation.getDuration());
        presentationEntity.setStatus(PresentationStatusEntity.valueOf(presentation.getStatus().name()));
        presentationEntity.setRegistrations(remapUserMap(presentation.getRegistrations()));
        presentationEntity.setRatings(remapUserMap(presentation.getRatings()));
        return presentationEntity;
    }

    private <T> Map<User, T> remapUserEntityMap(Map<UserEntity, T> tMap) {
        Map<User, T> mapped = new HashMap<>();
        Set<UserEntity> keys = tMap.keySet();
        for (UserEntity userEntity : keys) {
            mapped.put(userMapper.mapEntityToUser(userEntity), tMap.get(userEntity));
        }
        return mapped;
    }

    private <T> Map<UserEntity, T> remapUserMap(Map<User, T> tMap) {
        Map<UserEntity, T> mapped = new HashMap<>();
        Set<User> keys = tMap.keySet();
        for (User user : keys) {
            mapped.put(userMapper.mapUserToEntity(user), tMap.get(user));
        }
        return mapped;
    }
}
