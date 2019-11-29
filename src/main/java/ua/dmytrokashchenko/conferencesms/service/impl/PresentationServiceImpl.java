package ua.dmytrokashchenko.conferencesms.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.PresentationEntity;
import ua.dmytrokashchenko.conferencesms.entity.PresentationStatusEntity;
import ua.dmytrokashchenko.conferencesms.entity.UserEntity;
import ua.dmytrokashchenko.conferencesms.repository.PresentationRepository;
import ua.dmytrokashchenko.conferencesms.service.PresentationService;
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

    public PresentationServiceImpl(PresentationRepository presentationRepository, PresentationMapper presentationMapper, UserMapper userMapper) {
        this.presentationRepository = presentationRepository;
        this.presentationMapper = presentationMapper;
        this.userMapper = userMapper;
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
}
