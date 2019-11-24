package ua.dmytrokashchenko.conferencesms.service.mapper;

import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    private final AddressMapper addressMapper;
    private final PresentationMapper presentationMapper;

    public EventMapper(AddressMapper addressMapper, PresentationMapper presentationMapper) {
        this.addressMapper = addressMapper;
        this.presentationMapper = presentationMapper;
    }

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
        List<PresentationEntity> presentationEntities = event.getPresentations()
                .stream()
                .map(x -> presentationMapper.mapPresentationToEntity(x, event.getId()))
                .collect(Collectors.toList());

        return new EventEntity(event.getId(), event.getName(),
                event.getEventDetails(), event.getStartDate(),
                event.getFinishDate(), addressMapper.mapAddressToEntity(event.getAddress()), presentationEntities);
    }
}
