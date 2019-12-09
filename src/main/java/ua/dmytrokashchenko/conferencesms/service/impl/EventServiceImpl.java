package ua.dmytrokashchenko.conferencesms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.entity.EventEntity;
import ua.dmytrokashchenko.conferencesms.repository.EventRepository;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.EventServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.EventMapper;
import ua.dmytrokashchenko.conferencesms.service.validator.Validator;

import java.time.LocalDateTime;
import java.util.function.BiFunction;


@Log4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Validator<Event> eventValidator;

    @Override
    public void save(Event event) {
        eventValidator.validate(event);
        eventRepository.save(eventMapper.mapEventToEntity(event));
    }

    @Override
    public Event getById(Long id) {
        if (id == null || id < 0) {
            log.warn("Invalid id");
            throw new EventServiceException("Invalid id");
        }
        if (!eventRepository.findById(id).isPresent()) {
            log.info("No event with this id");
            throw new EventServiceException("No event with this id");
        }
        return eventMapper.mapEntityToEvent(eventRepository.findById(id).get());
    }

    @Override
    public Event getEventByPresentationId(Long id) {
        if (id == null || id < 0) {
            log.warn("Invalid id");
            throw new EventServiceException("Invalid id");
        }
        if (!eventRepository.findEventEntityByPresentationEntityId(id).isPresent()) {
            log.info("No event with this presentation");
            throw new EventServiceException("No event with this presentation");
        }
        return eventMapper.mapEntityToEvent(eventRepository.findEventEntityByPresentationEntityId(id).get());
    }

    @Override
    public Page<Event> getUpcomingEvents(Pageable pageable) {
        return getEvents(pageable, eventRepository::findEventEntitiesByFinishDateAfter);
    }

    @Override
    public Page<Event> getPastEvents(Pageable pageable) {
        return getEvents(pageable, eventRepository::findEventEntitiesByStartDateBefore);
    }

    private Page<Event> getEvents(Pageable pageable, BiFunction<LocalDateTime, Pageable, Page<EventEntity>> function) {
        if (pageable == null) {
            log.warn("Pageable object invalid");
            throw new EventServiceException("Pageable object invalid");
        }
        LocalDateTime now = LocalDateTime.now();
        return function.apply(now, pageable)
                .map(eventMapper::mapEntityToEvent);
    }
}
