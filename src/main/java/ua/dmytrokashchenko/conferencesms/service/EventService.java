package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.dmytrokashchenko.conferencesms.domain.Event;

public interface EventService {

    void add(Event event);
//
//    void update(Event event);
//
    Event getById(Long id);
//
//    Page<Event> getEvents(Pageable pageable);

    Page<Event> getUpcomingEvents(Pageable pageable);

    Page<Event> getPastEvents(Pageable pageable);

}
