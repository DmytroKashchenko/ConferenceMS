package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.User;

import java.util.List;

@Service
public interface PresentationService {

    Presentation getPresentationById(Long id);

    List<Presentation> getPresentationsByAuthorAndStatus(User author, PresentationStatus status);

}
