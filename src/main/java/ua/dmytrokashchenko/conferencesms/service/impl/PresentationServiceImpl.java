package ua.dmytrokashchenko.conferencesms.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;
import ua.dmytrokashchenko.conferencesms.repository.PresentationRepository;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.PresentationService;
import ua.dmytrokashchenko.conferencesms.service.UserService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.PresentationServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.PresentationMapper;
import ua.dmytrokashchenko.conferencesms.service.mapper.UserMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PresentationServiceImpl implements PresentationService {
    private static final Logger LOGGER = Logger.getLogger(PresentationServiceImpl.class);

    private final PresentationRepository presentationRepository;
    private final PresentationMapper presentationMapper;
    private final UserMapper userMapper;
    private final EventService eventService;
    private final UserService userService;

    public PresentationServiceImpl(PresentationRepository presentationRepository,
                                   PresentationMapper presentationMapper, UserMapper userMapper,
                                   EventService eventService, UserService userService) {
        this.presentationRepository = presentationRepository;
        this.presentationMapper = presentationMapper;
        this.userMapper = userMapper;
        this.eventService = eventService;
        this.userService = userService;
    }

    @Override
    public Presentation getPresentationById(Long id) {
        if (id == null) {
            LOGGER.warn("Invalid id");
            throw new PresentationServiceException("Invalid id");
        }
        Optional<PresentationEntity> optionalEntity = presentationRepository.findById(id);
        if (!optionalEntity.isPresent()) {
            LOGGER.warn("No presentations with ID: " + id);
            throw new PresentationServiceException("No presentations with ID:" + id);
        }
        PresentationEntity presentationEntity = optionalEntity.get();
        return presentationMapper.mapEntityToPresentation(presentationEntity);
    }

    @Override
    public List<Presentation> getPresentationsByAuthorAndStatus(User author, PresentationStatus status) {
        if (author == null || status == null) {
            return Collections.emptyList();
        }
        PresentationStatusEntity presentationStatusEntity = PresentationStatusEntity.valueOf(status.name());
        UserEntity userEntity = userMapper.mapUserToEntity(author);
        return presentationRepository.findPresentationEntityByAuthorAndStatus(userEntity, presentationStatusEntity)
                .stream()
                .map(presentationMapper::mapEntityToPresentation)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Long> getPresentationsIdsOnUserIsRegistered(Long userId) {
        if (userId == null || userId <= 0) {
            LOGGER.warn("Invalid id");
            throw new PresentationServiceException("Invalid id");
        }
        return presentationRepository.findPresentationsIdsOnUserIsRegistered(userId);
    }

    @Override
    public void registerForPresentation(Long presentationId, User user) {
        Event event = eventService.getEventByPresentationId(presentationId);
        Presentation presentation = event.getPresentationById(presentationId);
        if (presentation.getRegistrations().get(user) != null) {
            LOGGER.warn("User is already registered for the presentation");
            throw new PresentationServiceException("User is already registered for the presentation");
        }
        presentation.addRegistration(user);
        eventService.save(event);
    }

    @Override
    public void ratePresentation(User user, Integer rating, Long presentationId) {
        Event event = eventService.getEventByPresentationId(presentationId);
        Presentation presentation = event.getPresentationById(presentationId);
        if (presentation.getRegistrations().get(user) == null || !presentation.getRegistrations().get(user)) {
            LOGGER.warn("User isn't a visitor");
            throw new PresentationServiceException("User isn't a visitor");
        }
        presentation.addUserRating(user, rating);
        eventService.save(event);
    }

    @Override
    public void registerVisitor(Long presentationId, Long userId) {
        Event event = eventService.getEventByPresentationId(presentationId);
        Presentation presentation = event.getPresentationById(presentationId);
        User user = userService.getById(userId);
        if (presentation.getRegistrations().get(user) == null) {
            LOGGER.warn("User not registered");
            throw new PresentationServiceException("User not registered");
        }
        if (presentation.getRegistrations().get(user)) {
            LOGGER.warn("User is already a visitor");
            throw new PresentationServiceException("User is already a visitor");
        }
        presentation.getRegistrations().put(user, true);
        eventService.save(event);
    }
}
