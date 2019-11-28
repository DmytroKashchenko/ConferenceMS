package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.dmytrokashchenko.conferencesms.domain.Event;

public interface EventService {

    void save(Event event);

    Event getById(Long id);

    Event getEventByPresentationId(Long id);

    Page<Event> getUpcomingEvents(Pageable pageable);

    Page<Event> getPastEvents(Pageable pageable);

}
