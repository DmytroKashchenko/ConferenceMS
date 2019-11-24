package ua.dmytrokashchenko.conferencesms.service.impl;

import ua.dmytrokashchenko.conferencesms.service.PresentationService;

public class PresentationServiceImpl implements PresentationService {
    /*private static final Logger LOGGER = Logger.getLogger(PresentationServiceImpl.class);

    private final PresentationDao presentationDao;
    private final PresentationMapper presentationMapper;

    public PresentationServiceImpl(PresentationDao presentationDao, PresentationMapper presentationMapper) {
        this.presentationDao = presentationDao;
        this.presentationMapper = presentationMapper;
    }

    @Override
    public Presentation getPresentationById(Long id) {
        if (id == null) {
            LOGGER.warn("Invalid id");
            throw new PresentationServiceException("Invalid id");
        }
        Optional<PresentationEntity> optionalEntity = presentationDao.findById(id);
        if (!optionalEntity.isPresent()) {
            LOGGER.warn("No presentations with ID: " + id);
            throw new PresentationServiceException("No presentations with ID:" + id);
        }
        PresentationEntity presentationEntity = optionalEntity.get();
        return presentationMapper.mapEntityToPresentation(presentationEntity);
    }

    @Override
    public List<Presentation> getPresentationsSuggestedBySpeakerByEventId(Long eventId, Page page) {
        int statusId = PresentationStatus.SUGGESTED_BY_SPEAKER.ordinal();
        return getPresentationsByEventIdAndStatusId(eventId, page, statusId);
    }

    @Override
    public List<Presentation> getPresentationsSuggestedByModeratorByEventId(Long eventId, Page page) {
        int statusId = PresentationStatus.SUGGESTED_BY_SPEAKER.ordinal();
        return getPresentationsByEventIdAndStatusId(eventId, page, statusId);
    }

    private List<Presentation> getPresentationsByEventIdAndStatusId(Long eventId, Page page, int statusId) {
        if (eventId == null) {
            LOGGER.warn("Invalid event id");
            throw new PresentationServiceException("Invalid event id");
        }
        if (page == null) {
            LOGGER.warn("Invalid page");
            throw new PresentationServiceException("Invalid page");
        }
        return presentationDao.findPresentationsByEventIdAndStatus(eventId, page, (long) statusId)
                .stream()
                .map(presentationMapper::mapEntityToPresentation)
                .collect(Collectors.toList());
    }*/
}
