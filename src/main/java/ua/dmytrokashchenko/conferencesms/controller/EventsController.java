package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.domain.*;
import ua.dmytrokashchenko.conferencesms.service.AddressService;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.PresentationService;
import ua.dmytrokashchenko.conferencesms.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class EventsController {
    private static final Sort ORDER_FOR_EVENTS = Sort.by("startDate");

    private final EventService eventService;
    private final AddressService addressService;
    private final UserService userService;
    private final PresentationService presentationService;

    public EventsController(EventService eventService, AddressService addressService,
                            UserService userService, PresentationService presentationService) {
        this.eventService = eventService;
        this.addressService = addressService;
        this.userService = userService;
        this.presentationService = presentationService;
    }

    @GetMapping("/events_upcoming")
    public ModelAndView upcomingEvents(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = getPageable(pageNum, pageSize);
        Page<Event> events = eventService.getUpcomingEvents(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("events_upcoming");
        modelAndView.addObject("events", events);

        return modelAndView;
    }

    @GetMapping("/events_past")
    public ModelAndView pastEvents(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = getPageable(pageNum, pageSize);
        Page<Event> events = eventService.getPastEvents(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("events_past");
        modelAndView.addObject("events", events);

        return modelAndView;
    }

    @PostMapping({"/event_add", "/event_edit"})
    public String addEvent(@RequestParam Long addressId,
                           @RequestParam String eventName,
                           @RequestParam String eventDetails,
                           @RequestParam("eventStart")
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStart,
                           @RequestParam("eventFinish")
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventFinish,
                           @RequestParam(required = false) Long eventId) {

        Address address = addressService.getAddressById(addressId);

        Event event = Event.builder()
                .id(eventId)
                .address(address)
                .name(eventName)
                .eventDetails(eventDetails)
                .startDate(eventStart)
                .finishDate(eventFinish)
                .build();
        eventService.save(event);

        return "redirect:/event_management";
    }

    @GetMapping("/event_add")
    public String addEvent() {
        return "event_add";
    }

    @GetMapping("/event_management")
    public ModelAndView eventManagement(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = getPageable(pageNum, pageSize);
        Page<Event> events = eventService.getUpcomingEvents(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("event_management");
        modelAndView.addObject("events", events);
        return modelAndView;
    }

    @GetMapping("/event_edit")
    public ModelAndView eventEdit(@RequestParam Long eventId) {
        Event event = eventService.getById(eventId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("event_edit");
        modelAndView.addObject("event", event);
        return modelAndView;
    }

    @GetMapping("/presentation_add")
    public ModelAndView presentationSave(@RequestParam Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("presentation_add");
        modelAndView.addObject("eventId", eventId);
        return modelAndView;
    }

    @PostMapping("/presentation_add")
    public String presentationSave(@RequestParam String authorEmail,
                                   @RequestParam String presentationTopic,
                                   @RequestParam String presentationDescription,
                                   @RequestParam
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                           LocalDateTime presentationStart,
                                   @RequestParam Integer presentationDuration,
                                   @RequestParam Long eventId,
                                   @RequestParam String presentationStatus) {

        User author = userService.getByEmail(authorEmail);
        Event event = eventService.getById(eventId);
        Duration duration = Duration.ofMinutes(presentationDuration);
        PresentationStatus status;
        if (presentationStatus != null && presentationStatus.equals("Confirmed")) {
            status = PresentationStatus.CONFIRMED;
        } else if (presentationStatus != null && presentationStatus.equals("Suggested by moderator")) {
            status = PresentationStatus.SUGGESTED_BY_MODERATOR;
        } else {
            status = PresentationStatus.REJECTED;
        }
        Presentation presentation = Presentation.builder()
                .author(author)
                .topic(presentationTopic)
                .description(presentationDescription)
                .startDate(presentationStart)
                .duration(duration)
                .status(status)
                .build();

        event.addPresentation(presentation);
        eventService.save(event);

        return "redirect:event_management";
    }

    @GetMapping("/presentation_edit")
    public ModelAndView presentationEdit(@RequestParam Long presentationId, @RequestParam Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("presentation_edit");
        Event event = eventService.getById(eventId);
        Presentation presentation = null;
        for (Presentation presentationFromEvent : event.getPresentations()) {
            if (presentationFromEvent.getId().equals(presentationId)) {
                presentation = presentationFromEvent;
            }
        }
        modelAndView.addObject("presentation", presentation);
        modelAndView.addObject("eventId", eventId);
        return modelAndView;
    }

    @PostMapping("/presentation_edit")
    public String presentationEdit(@RequestParam String authorEmail,
                                   @RequestParam String presentationTopic,
                                   @RequestParam String presentationDescription,
                                   @RequestParam
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                           LocalDateTime presentationStart,
                                   @RequestParam Integer presentationDuration,
                                   @RequestParam Long eventId,
                                   @RequestParam Long presentationId) {

        User author = userService.getByEmail(authorEmail);
        Event event = eventService.getById(eventId);
        Presentation presentation = null;
        for (Presentation presentationFromEvent : event.getPresentations()) {
            if (presentationFromEvent.getId().equals(presentationId)) {
                presentation = presentationFromEvent;
            }
        }
        Duration duration = Duration.ofMinutes(presentationDuration);
        Assert.notNull(presentation, "Invalid presentation ID");
        presentation.setAuthor(author);
        presentation.setTopic(presentationTopic);
        presentation.setDescription(presentationDescription);
        presentation.setStartDate(presentationStart);
        presentation.setDuration(duration);
        eventService.save(event);

        return "redirect:event_management/" + eventId;
    }

    @GetMapping("/event_management/{eventId}")
    public ModelAndView eventDetailsManagement(@PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("event_details_management");
        Event event = eventService.getById(eventId);
        List<Presentation> presentations = event.getPresentationsByStatus(PresentationStatus.CONFIRMED);
        modelAndView.addObject("event", event);
        modelAndView.addObject("presentations", presentations);
        return modelAndView;
    }

    @GetMapping("/events/upcoming/{eventId}")
    public ModelAndView upcomingEventDetails(@AuthenticationPrincipal User user,
                                             @PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upcoming_event_details");
        Event event = eventService.getById(eventId);
        List<Presentation> presentations = event.getPresentationsByStatus(PresentationStatus.CONFIRMED);
        Set<Long> presentationsIds = presentationService.getPresentationsIdsOnUserIsRegistered(user.getId());
        modelAndView.addObject("presentationIds", presentationsIds);
        modelAndView.addObject("event", event);
        modelAndView.addObject("presentations", presentations);
        return modelAndView;
    }

    @GetMapping("/event_management/{eventId}/suggested_by_speaker")
    public ModelAndView eventSuggestedBySpeakerPresentations(@PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("presentations_suggested_by_speaker");
        Event event = eventService.getById(eventId);
        List<Presentation> presentations = event.getPresentationsByStatus(PresentationStatus.SUGGESTED_BY_SPEAKER);
        modelAndView.addObject("presentations", presentations);
        return modelAndView;
    }

    @PostMapping("/event_management/{eventId}/suggested_by_speaker")
    public String confirmPresentation(@PathVariable Long eventId, @RequestParam Long presentationId) {
        Event event = eventService.getById(eventId);
        Presentation presentation = event.getPresentationById(presentationId);
        if (presentation != null) {
            presentation.setStatus(PresentationStatus.CONFIRMED);
        }
        eventService.save(event);
        return "redirect:/event_management/" + eventId + "/suggested_by_speaker";
    }

    @GetMapping("/event_management/{eventId}/register_visitors/{presentationId}/all")
    public ModelAndView registerVisitors(@PathVariable Long presentationId,
                                         @PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_visitors_all");
        Event event = eventService.getById(eventId);
        Presentation presentation = event.getPresentationById(presentationId);
        Set<User> users = new TreeSet<>(presentation.getRegistrations().keySet());
        Map<User, Boolean> registrations = presentation.getRegistrations();
        modelAndView.addObject("users", users);
        modelAndView.addObject("registrations", registrations);
        modelAndView.addObject("presentation", presentation);
        modelAndView.addObject("eventId", eventId);
        return modelAndView;
    }

    @PostMapping("/register_visitor")
    public String registerVisitor(@RequestParam Long eventId,
                                  @RequestParam Long presentationId,
                                  @RequestParam Long userId) {
        Event event = eventService.getById(eventId);
        Presentation presentation = event.getPresentationById(presentationId);
        User user = userService.getById(userId);
        if (presentation.getRegistrations().get(user) == null) {
            return "redirect:/"; //TODO need to add processing
        }
        if (presentation.getRegistrations().get(user)) {
            return "redirect:/";  //TODO need to add processing
        }
        presentation.getRegistrations().put(user, true);
        eventService.save(event);
        return "redirect:/event_management/" + eventId + "/register_visitors/" + presentationId + "/all";
    }


    private Pageable getPageable(Integer pageNum, Integer pageSize) {
        pageNum = Math.max(pageNum, 1);
        pageSize = Math.max(pageSize, 1);
        pageNum--;
        return PageRequest.of(pageNum, pageSize, ORDER_FOR_EVENTS);
    }
}
