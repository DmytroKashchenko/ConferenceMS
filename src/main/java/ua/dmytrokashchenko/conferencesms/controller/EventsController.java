package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.PresentationStatus;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.PresentationService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class EventsController {
    private final EventService eventService;
    private final PresentationService presentationService;

    public EventsController(EventService eventService, PresentationService presentationService) {
        this.eventService = eventService;
        this.presentationService = presentationService;
    }

    @GetMapping("/")
    public ModelAndView upcomingEvents(
            @PageableDefault(sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Event> events = eventService.getUpcomingEvents(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("events_upcoming");
        modelAndView.addObject("events", events);
        return modelAndView;
    }

    @GetMapping("/events/past")
    public ModelAndView pastEvents(@PageableDefault Pageable pageable) {
        Page<Event> events = eventService.getPastEvents(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("events_past");
        modelAndView.addObject("events", events);
        return modelAndView;
    }

    @GetMapping("/events/upcoming/{eventId}")
    public ModelAndView upcomingEventDetails(@AuthenticationPrincipal User user,
                                             @PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upcoming_event_details");
        Set<Long> presentationsIds;
        if (user != null) {
            presentationsIds = presentationService.getPresentationsIdsOnUserIsRegistered(user.getId());
        } else {
            presentationsIds = Collections.emptySet();
        }
        modelAndView.addObject("presentationIds", presentationsIds);
        Event event = eventService.getById(eventId);
        List<Presentation> presentations = event.getPresentationsByStatus(PresentationStatus.CONFIRMED);
        modelAndView.addObject("event", event);
        modelAndView.addObject("presentations", presentations);
        return modelAndView;
    }

    @GetMapping("/events/past/{eventId}")
    public ModelAndView pastEventDetails(@AuthenticationPrincipal User user,
                                         @PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("past_event_details");
        Event event = eventService.getById(eventId);
        List<Presentation> presentations = event.getPresentationsByStatus(PresentationStatus.CONFIRMED);
        modelAndView.addObject("event", event);
        modelAndView.addObject("presentations", presentations);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
