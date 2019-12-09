package ua.dmytrokashchenko.conferencesms.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final AddressMapper addressMapper;
    private final PresentationMapper presentationMapper;

    public Event mapEntityToEvent(EventEntity eventEntity) {
        return Event.builder()
                .id(eventEntity.getId())
                .name(eventEntity.getName())
                .eventDetails(eventEntity.getEventDetails())
                .startDate(eventEntity.getStartDate())
                .finishDate(eventEntity.getFinishDate())
                .address(addressMapper.mapEntityToAddress(eventEntity.getAddressEntity()))
                .presentations(eventEntity.getPresentationEntities()
                        .stream()
                        .map(presentationMapper::mapEntityToPresentation)
                        .collect(Collectors.toList()))
                .build();
    }

    public EventEntity mapEventToEntity(Event event) {
        List<PresentationEntity> presentationEntities;
        if (event.getPresentations() != null) {
            presentationEntities = event.getPresentations()
                    .stream()
                    .map(presentationMapper::mapPresentationToEntity)
                    .collect(Collectors.toList());
        } else {
            presentationEntities = new ArrayList<>();
        }

        return new EventEntity(event.getId(), event.getName(),
                event.getEventDetails(), event.getStartDate(),
                event.getFinishDate(), addressMapper.mapAddressToEntity(event.getAddress()), presentationEntities);
    }
}
