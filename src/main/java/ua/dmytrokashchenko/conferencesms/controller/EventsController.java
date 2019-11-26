package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.service.AddressService;
import ua.dmytrokashchenko.conferencesms.service.EventService;

import java.time.LocalDateTime;

@Controller
public class EventsController {
    private static final Sort ORDER_FOR_EVENTS = Sort.by("startDate");

    private final EventService eventService;
    private final AddressService addressService;

    public EventsController(EventService eventService, AddressService addressService) {
        this.eventService = eventService;
        this.addressService = addressService;
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

    @PostMapping("/event_add")
    public String addEvent(@RequestParam Long addressId,
                           @RequestParam String eventName,
                           @RequestParam String eventDetails,
                           @RequestParam("eventStart")
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStart,
                           @RequestParam("eventFinish")
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventFinish) {

        Address address = addressService.getAddressById(addressId);

        Event event = Event.builder()
                .address(address)
                .name(eventName)
                .eventDetails(eventDetails)
                .startDate(eventStart)
                .finishDate(eventFinish)
                .build();
        eventService.add(event);

        return "redirect:/events_upcoming";
    }

    @GetMapping("/event_add")
    public String addEvent(){
        return "event_add";
    }

    private Pageable getPageable(Integer pageNum, Integer pageSize) {
        pageNum = Math.max(pageNum, 1);
        pageSize = Math.max(pageSize, 1);
        pageNum--;
        return PageRequest.of(pageNum, pageSize, ORDER_FOR_EVENTS);
    }
}
