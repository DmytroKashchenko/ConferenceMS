package ua.dmytrokashchenko.conferencesms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;
import ua.dmytrokashchenko.conferencesms.repository.PresentationRepository;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.PresentationService;
import ua.dmytrokashchenko.conferencesms.service.UserService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.PresentationServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.PresentationMapper;
import ua.dmytrokashchenko.conferencesms.service.mapper.UserMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j
@Service
@RequiredArgsConstructor
public class PresentationServiceImpl implements PresentationService {
    private final PresentationRepository presentationRepository;
    private final PresentationMapper presentationMapper;
    private final UserMapper userMapper;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public List<Presentation> getPresentationsByAuthorAndStatus(User author, PresentationStatus status) {
        if (author == null || status == null) {
            log.warn("Invalid author or status");
            throw new PresentationServiceException("Invalid author or status");
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
            log.warn("Invalid id");
            throw new PresentationServiceException("Invalid id");
        }
        return presentationRepository.findPresentationsIdsOnUserIsRegistered(userId);
    }

    @Override
    public void registerForPresentation(Long presentationId, User user) {
        if (presentationId == null || user == null) {
            log.warn("Invalid user or presentation id");
            throw new PresentationServiceException("Invalid user or presentation id");
        }
        Event event = eventService.getEventByPresentationId(presentationId);
        Presentation presentation = event.getPresentationById(presentationId);
        if (presentation == null) {
            log.warn("No such presentation");
            throw new PresentationServiceException("No such presentation");
        }
        if (presentation.getRegistrations().get(user) != null) {
            log.info("User is already registered for the presentation");
            throw new PresentationServiceException("User is already registered for the presentation");
        }
        presentation.addRegistration(user);
        eventService.save(event);
    }

    @Override
    public void ratePresentation(User user, Integer rating, Long presentationId) {
        if (user == null || rating == null) {
            log.warn("Invalid rating or user");
            throw new PresentationServiceException("Invalid rating or user");
        }
        Event event = eventService.getEventByPresentationId(presentationId);
        Presentation presentation = event.getPresentationById(presentationId);
        if (presentation == null) {
            log.warn("No such presentation");
            throw new PresentationServiceException("No such presentation");
        }
        if (presentation.getRegistrations().get(user) == null || !presentation.getRegistrations().get(user)) {
            log.warn("User isn't a visitor");
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
            log.warn("User not registered");
            throw new PresentationServiceException("User not registered");
        }
        if (presentation.getRegistrations().get(user)) {
            log.warn("User is already a visitor");
            throw new PresentationServiceException("User is already a visitor");
        }
        presentation.getRegistrations().put(user, true);
        eventService.save(event);
    }
}
