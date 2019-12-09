package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.User;

import java.util.List;
import java.util.Set;

@Service
public interface PresentationService {
    
    List<Presentation> getPresentationsByAuthorAndStatus(User author, PresentationStatus status);

    Set<Long> getPresentationsIdsOnUserIsRegistered(Long userId);

    void registerForPresentation(Long presentationId, User user);

    void ratePresentation(User user, Integer rating, Long presentationId);

    void registerVisitor(Long presentationId, Long userId);
}
