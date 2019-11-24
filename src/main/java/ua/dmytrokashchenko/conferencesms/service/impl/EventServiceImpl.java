package ua.dmytrokashchenko.conferencesms.service.impl;

import org.apache.log4j.Logger;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.repository.EventRepository;
import ua.dmytrokashchenko.conferencesms.repository.PresentationRepository;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.EventServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.EventMapper;
import ua.dmytrokashchenko.conferencesms.service.mapper.PresentationMapper;
import ua.dmytrokashchenko.conferencesms.service.util.CollectionUtil;
import ua.dmytrokashchenko.conferencesms.service.validator.Validator;

import java.util.List;

public class EventServiceImpl implements EventService {
    /*private static final Logger LOGGER = Logger.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final Validator<Event> eventValidator;
    private final EventMapper eventMapper;
    private final PresentationRepository presentationRepository;
    private final PresentationMapper presentationMapper;

    public EventServiceImpl(EventRepository eventRepository, Validator<Event> eventValidator, EventMapper eventMapper,
                            PresentationRepository presentationRepository, PresentationMapper presentationMapper) {
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
        this.eventMapper = eventMapper;
        this.presentationRepository = presentationRepository;
        this.presentationMapper = presentationMapper;
    }

    @Override
    public void add(Event event) {
        eventValidator.validate(event);
        eventRepository.save(eventMapper.mapEventToEntity(event));
    }

    //TODO !!!
    @Override
    public void update(Event event) {
        if (event.getId() == null) {
            LOGGER.warn("Unsaved event cannot be updated");
            throw new EventServiceException("Unsaved event cannot be updated");
        }
        eventValidator.validate(event);
        List<Presentation> savedPresentations = getById(event.getId()).getPresentations();
        List<Presentation> presentationsForUpdate = event.getPresentations();

        if (savedPresentations.size() > presentationsForUpdate.size()) {
            List<Presentation> difference = CollectionUtil.difference(savedPresentations, presentationsForUpdate);
            if (difference.size() != 1) {
                LOGGER.warn("More than one presentation cannot be deleted");
                throw new EventServiceException("More than one presentation cannot be deleted");
            }
            Presentation presentation = difference.get(0);
            presentationRepository.deleteById(presentation.getId());
        } else if (savedPresentations.size() < presentationsForUpdate.size()) {
            List<Presentation> difference = CollectionUtil.difference(presentationsForUpdate, savedPresentations);
            if (difference.size() != 1) {
                LOGGER.warn("More than one presentation cannot be added");
                throw new EventServiceException("More than one presentation cannot be added");
            }
            Presentation presentation = difference.get(0);
            if (presentation.getId() != null) {
                presentationRepository.save(presentationMapper.mapPresentationToEntity(presentation, event.getId()));
            } else {
                presentationRepository.save(presentationMapper.mapPresentationToEntity(presentation, event.getId()));
            }
        } else {
            List<Presentation> difference = CollectionUtil.difference(presentationsForUpdate, savedPresentations);
            if (difference.size() != 0) {
                if (difference.size() > 1) {
                    LOGGER.warn("More than one presentation cannot be updated");
                    throw new EventServiceException("More than one presentation cannot be updated");
                }
                Presentation presentation = difference.get(0);
                presentationRepository.save(presentationMapper.mapPresentationToEntity(presentation, event.getId()));
            }
        }
        eventRepository.save(eventMapper.mapEventToEntity(event));
    }

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

    @Override
    public List<Presentation> getPresentationsByEventId(Long eventId, Page page) {
        return presentationRepository.findPresentationsByEventId(eventId, page)
                .stream()
                .map(presentationMapper::mapEntityToPresentation)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEvents(Page page) {
        return getEvents(page, eventRepository::findRecords);
    }

    @Override
    public List<Event> getPastEvents(Page page) {
        return getEvents(page, eventRepository::findPastEvents);
    }

    @Override
    public List<Event> getUpcomingEvents(Page page) {
        return getEvents(page, eventRepository::findUpcomingEvents);
    }

    private List<Event> getEvents(Page page, Function<Page, List<EventEntity>> function) {
        if (page == null) {
            LOGGER.warn("Invalid page");
            throw new EventServiceException("Invalid page");
        }
        return function.apply(page)
                .stream()
                .map(eventMapper::mapEntityToEvent)
                .collect(Collectors.toList());
    }*/
}
