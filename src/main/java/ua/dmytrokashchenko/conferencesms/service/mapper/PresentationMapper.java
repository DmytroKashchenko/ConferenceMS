package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;

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
                .build();
    }

    public PresentationEntity mapPresentationToEntity(Presentation presentation, Long eventId) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(eventId);

        return new PresentationEntity(presentation.getId(),
                userMapper.mapUserToEntity(presentation.getAuthor()),
                presentation.getTopic(),
                presentation.getDescription(),
                presentation.getStartDate(),
                presentation.getDuration(),
                eventEntity,
                PresentationStatusEntity.valueOf(presentation.getStatus().name()));
    }
}
