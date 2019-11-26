package ua.dmytrokashchenko.conferencesms.service.impl;

import org.apache.log4j.Logger;
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

@Service
public class EventServiceImpl implements EventService {
    private static final Logger LOGGER = Logger.getLogger(EventServiceImpl.class);

//    private final EventRepository eventRepository;
//    private final Validator<Event> eventValidator;
//    private final EventMapper eventMapper;
//    private final PresentationRepository presentationRepository;
//    private final PresentationMapper presentationMapper;
//
//    public EventServiceImpl(EventRepository eventRepository, Validator<Event> eventValidator, EventMapper eventMapper,
//                            PresentationRepository presentationRepository, PresentationMapper presentationMapper) {
//        this.eventRepository = eventRepository;
//        this.eventValidator = eventValidator;
//        this.eventMapper = eventMapper;
//        this.presentationRepository = presentationRepository;
//        this.presentationMapper = presentationMapper;
//    }

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Validator<Event> eventValidator;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, Validator<Event> eventValidator) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventValidator = eventValidator;
    }

    @Override
    public void add(Event event) {
        eventValidator.validate(event);
        eventRepository.save(eventMapper.mapEventToEntity(event));
    }
//
//    //TODO !!!
//    @Override
//    public void update(Event event) {
//        if (event.getId() == null) {
//            LOGGER.warn("Unsaved event cannot be updated");
//            throw new EventServiceException("Unsaved event cannot be updated");
//        }
//        eventValidator.validate(event);
//        List<Presentation> savedPresentations = getById(event.getId()).getPresentations();
//        List<Presentation> presentationsForUpdate = event.getPresentations();
//
//        if (savedPresentations.size() > presentationsForUpdate.size()) {
//            List<Presentation> difference = CollectionUtil.difference(savedPresentations, presentationsForUpdate);
//            if (difference.size() != 1) {
//                LOGGER.warn("More than one presentation cannot be deleted");
//                throw new EventServiceException("More than one presentation cannot be deleted");
//            }
//            Presentation presentation = difference.get(0);
//            presentationRepository.deleteById(presentation.getId());
//        } else if (savedPresentations.size() < presentationsForUpdate.size()) {
//            List<Presentation> difference = CollectionUtil.difference(presentationsForUpdate, savedPresentations);
//            if (difference.size() != 1) {
//                LOGGER.warn("More than one presentation cannot be added");
//                throw new EventServiceException("More than one presentation cannot be added");
//            }
//            Presentation presentation = difference.get(0);
//            if (presentation.getId() != null) {
//                presentationRepository.save(presentationMapper.mapPresentationToEntity(presentation, event.getId()));
//            } else {
//                presentationRepository.save(presentationMapper.mapPresentationToEntity(presentation, event.getId()));
//            }
//        } else {
//            List<Presentation> difference = CollectionUtil.difference(presentationsForUpdate, savedPresentations);
//            if (difference.size() != 0) {
//                if (difference.size() > 1) {
//                    LOGGER.warn("More than one presentation cannot be updated");
//                    throw new EventServiceException("More than one presentation cannot be updated");
//                }
//                Presentation presentation = difference.get(0);
//                presentationRepository.save(presentationMapper.mapPresentationToEntity(presentation, event.getId()));
//            }
//        }
//        eventRepository.save(eventMapper.mapEventToEntity(event));
//    }
//
    @Override
    public Event getById(Long id) {
        if (id == null || id < 0) {
            LOGGER.warn("Invalid id");
            throw new EventServiceException("Invalid id");
        }
        if (!eventRepository.findById(id).isPresent()) {
            LOGGER.warn("No event with this id");
            throw new EventServiceException("No event with this id");
        }
        return eventMapper.mapEntityToEvent(eventRepository.findById(id).get());
    }
//
//    @Override
//    public List<Presentation> getPresentationsByEventId(Long eventId, Page page) {
//        return presentationRepository.findPresentationsByEventId(eventId, page)
//                .stream()
//                .map(presentationMapper::mapEntityToPresentation)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Event> getEvents(Page page) {
//        return getEvents(page, eventRepository::findRecords);
//    }
//
//    @Override
//    public List<Event> getPastEvents(Page page) {
//        return getEvents(page, eventRepository::findPastEvents);
//    }
//
    @Override
    public Page<Event> getUpcomingEvents(Pageable pageable) {
        return getEvents(pageable, eventRepository::findEventEntitiesByStartDateAfter);
    }

    @Override
    public Page<Event> getPastEvents(Pageable pageable) {
        return getEvents(pageable, eventRepository::findEventEntitiesByStartDateBefore);
    }

    private Page<Event> getEvents(Pageable pageable, BiFunction<LocalDateTime, Pageable, Page<EventEntity>> function) {
        if (pageable == null) {
            LOGGER.warn("Pageable object is null");
            throw new EventServiceException("Pageable object is null");
        }
        LocalDateTime now = LocalDateTime.now();
        return function.apply(now, pageable)
                .map(eventMapper::mapEntityToEvent);
    }





//
//    private List<Event> getEvents(Page page, Function<Page, List<EventEntity>> function) {
//        if (page == null) {
//            LOGGER.warn("Invalid page");
//            throw new EventServiceException("Invalid page");
//        }
//        return function.apply(page)
//                .stream()
//                .map(eventMapper::mapEntityToEvent)
//                .collect(Collectors.toList());
//    }
}
