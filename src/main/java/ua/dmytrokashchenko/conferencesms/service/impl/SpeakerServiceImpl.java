package ua.dmytrokashchenko.conferencesms.service.impl;

import ua.dmytrokashchenko.conferencesms.service.SpeakerService;

public class SpeakerServiceImpl implements SpeakerService {
    /*private static final Logger LOGGER = Logger.getLogger(SpeakerServiceImpl.class);
    private final PresentationDao presentationDao;

    public SpeakerServiceImpl(PresentationDao presentationDao) {
        this.presentationDao = presentationDao;
    }

    @Override
    public void rateSpeaker(Speaker speaker, Long presentationId, Long userId) {
        PresentationEntity presentationEntity = presentationDao.findById(presentationId)
                .orElseThrow(() -> new RuntimeException("Invalid presentation id"));  //TODO need to add custom exception
        Long authorId = presentationEntity.getId();
        if (!authorId.equals(speaker.getId())){
            LOGGER.warn("Incorrect presentation id");
            throw new RuntimeException("Incorrect presentation id"); //TODO need to add custom exception
        }
        // TODO need to change or add new rating

        throw new UnsupportedOperationException(); // !!!
    }*/
}
