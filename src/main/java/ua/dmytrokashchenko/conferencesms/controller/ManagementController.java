package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.domain.*;
import ua.dmytrokashchenko.conferencesms.service.*;
import ua.dmytrokashchenko.conferencesms.service.util.BonusUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/management")
public class ManagementController {

    private final AddressService addressService;
    private final EventService eventService;
    private final UserService userService;
    private final BonusService bonusService;
    private final EmailSenderService emailSenderService;

    public ManagementController(AddressService addressService, EventService eventService,
                                UserService userService, BonusService bonusService,
                                EmailSenderService emailSenderService) {
        this.addressService = addressService;
        this.eventService = eventService;
        this.userService = userService;
        this.bonusService = bonusService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/address_add")
    public String addAddress() {
        return "address_add";
    }

    @GetMapping("/address_management")
    public ModelAndView addresses(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Address> addresses = addressService.getAddresses(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("address_management");
        modelAndView.addObject("addresses", addresses);
        return modelAndView;
    }

    @PostMapping({"/address_add", "/address_edit"})
    public String addAddress(
            @RequestParam String addressName,
            @RequestParam String addressCountry,
            @RequestParam String addressCity,
            @RequestParam String addressDetails,
            @RequestParam(required = false) Long addressId) {
        Address address = Address.builder()
                .id(addressId)
                .name(addressName)
                .country(addressCountry)
                .city(addressCity)
                .detailedAddress(addressDetails)
                .build();
        addressService.saveAddress(address);

        return "redirect:/management/address_management";
    }

    @GetMapping("/address_edit")
    public ModelAndView addressEdit(@RequestParam(name = "addressId") Long addressId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("address_edit");
        Address address = addressService.getAddressById(addressId);
        modelAndView.addObject("address", address);
        return modelAndView;
    }

    @GetMapping("/{eventId}/address_selection")
    public ModelAndView addressSelection(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("address_selection");
        Page<Address> addresses = addressService.getAddresses(pageable);
        modelAndView.addObject("addresses", addresses);
        modelAndView.addObject("eventId", eventId);
        return modelAndView;
    }

    @PostMapping("/address_selection")
    public String addressSelection(@RequestParam Long addressId, @RequestParam Long eventId) {
        Address address = addressService.getAddressById(addressId);
        Event event = eventService.getById(eventId);
        event.setAddress(address);
        eventService.save(event);
        return "redirect:/management";
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

        return "redirect:/management";
    }

    @GetMapping("/event_add")
    public String addEvent() {
        return "event_add";
    }

    @GetMapping
    public ModelAndView eventManagement(
            @PageableDefault(sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Event> events = eventService.getUpcomingEvents(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("event_management");
        modelAndView.addObject("events", events);
        return modelAndView;
    }

    @GetMapping("/event_edit/{eventId}")
    public ModelAndView eventEdit(@PathVariable Long eventId) {
        Event event = eventService.getById(eventId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("event_edit");
        modelAndView.addObject("event", event);
        return modelAndView;
    }

    @GetMapping("/{eventId}/presentation_add")
    public ModelAndView presentationSave(@PathVariable Long eventId) {
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

        return "redirect:/management";
    }

    @GetMapping("/{eventId}/presentation_edit/{presentationId}")
    public ModelAndView presentationEdit(@PathVariable Long presentationId, @PathVariable Long eventId) {
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

        return "redirect:" + eventId;
    }

    @GetMapping("/{eventId}")
    public ModelAndView eventDetailsManagement(@PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("event_details_management");
        Event event = eventService.getById(eventId);
        List<Presentation> presentations = event.getPresentationsByStatus(PresentationStatus.CONFIRMED);
        modelAndView.addObject("event", event);
        modelAndView.addObject("presentations", presentations);
        return modelAndView;
    }

    @GetMapping("/{eventId}/suggested_by_speaker")
    public ModelAndView eventSuggestedBySpeakerPresentations(@PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("presentations_suggested_by_speaker");
        Event event = eventService.getById(eventId);
        List<Presentation> presentations = event.getPresentationsByStatus(PresentationStatus.SUGGESTED_BY_SPEAKER);
        modelAndView.addObject("presentations", presentations);
        return modelAndView;
    }

    @PostMapping("/{eventId}/suggested_by_speaker")
    public String confirmPresentation(@PathVariable Long eventId, @RequestParam Long presentationId) {
        Event event = eventService.getById(eventId);
        Presentation presentation = event.getPresentationById(presentationId);
        if (presentation != null) {
            presentation.setStatus(PresentationStatus.CONFIRMED);
        }
        eventService.save(event);
        return String.format("redirect:/management/%d/suggested_by_speaker", eventId);
    }

    @GetMapping("/{eventId}/register_visitors/{presentationId}/all")
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
        return String.format("redirect:/management/%d/register_visitors/%d/all", eventId, presentationId);
    }

    @GetMapping("/speaker_rating")
    public ModelAndView showSpeakerRating(
            @PageableDefault(sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("speaker_rating");
        Page<User> speakers = userService.getUsersByRole(Role.SPEAKER, pageable);
        Map<User, Double> speakerRatings = userService.getSpeakerRatings(speakers.toSet());
        Set<Bonus> bonuses = bonusService.getRecords();
        Map<User, Double> coefficients = BonusUtil.getCoefficients(bonuses, speakerRatings);
        modelAndView.addObject("speakers", speakers);
        modelAndView.addObject("speakerRatings", speakerRatings);
        modelAndView.addObject("coefficients", coefficients);
        return modelAndView;
    }

    @GetMapping("/bonuses")
    public ModelAndView showBonusTable() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("bonuses");
        Set<Bonus> bonuses = bonusService.getRecords();
        modelAndView.addObject("bonuses", bonuses);
        return modelAndView;
    }

    @GetMapping("/send_email/{eventId}")
    public ModelAndView sendMessages(@PathVariable Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("eventId", eventId);
        modelAndView.setViewName("send_emails");
        return modelAndView;
    }

    @PostMapping("/send_email")
    public String sendMessages(@RequestParam Long eventId,
                               @RequestParam String subject,
                               @RequestParam String message) {
        Event event = eventService.getById(eventId);
        Set<String> emails = event.getPresentations().stream()
                .filter((x) -> x.getStatus().equals(PresentationStatus.CONFIRMED))
                .map(Presentation::getRegistrations)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .map(User::getEmail)
                .collect(Collectors.toSet());
        emailSenderService.sendMessages(emails, subject, message);
        return "redirect:/management";
    }
}
