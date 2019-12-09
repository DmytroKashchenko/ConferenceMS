package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.controller.form.PresentationForm;
import ua.dmytrokashchenko.conferencesms.domain.*;
import ua.dmytrokashchenko.conferencesms.service.*;
import ua.dmytrokashchenko.conferencesms.service.util.BonusUtil;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final PresentationService presentationService;

    public ManagementController(AddressService addressService, EventService eventService,
                                UserService userService, BonusService bonusService,
                                EmailSenderService emailSenderService, PresentationService presentationService) {
        this.addressService = addressService;
        this.eventService = eventService;
        this.userService = userService;
        this.bonusService = bonusService;
        this.emailSenderService = emailSenderService;
        this.presentationService = presentationService;
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
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                   LocalDate eventStartDate,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                   LocalDate eventFinishDate,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
                                   LocalTime eventStartTime,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
                                   LocalTime eventFinishTime,
                           @RequestParam(required = false) Long eventId) {
        Address address = addressService.getAddressById(addressId);

        LocalDateTime eventStart = LocalDateTime.of(eventStartDate, eventStartTime);
        LocalDateTime eventFinish = LocalDateTime.of(eventFinishDate, eventFinishTime);

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
    public ModelAndView addEvent(@RequestParam(required = false) Long addressId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("event_add");
        if (addressId != null) {
            Address address = addressService.getAddressById(addressId);
            modelAndView.addObject("address", address);
        }
        return modelAndView;
    }

    @GetMapping("/event_add/select_address")
    public ModelAndView addressSelection(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("address_selection_for_new_event");
        Page<Address> addresses = addressService.getAddresses(pageable);
        modelAndView.addObject("addresses", addresses);
        return modelAndView;
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
    public ModelAndView presentationSave(@PathVariable Long eventId, PresentationForm presentationForm) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("presentation_add");
        modelAndView.addObject("eventId", eventId);
        modelAndView.addObject("form", presentationForm);
        return modelAndView;
    }

    @PostMapping("/presentation_add")
    public String presentationSave(@Valid @ModelAttribute("form") PresentationForm form,
                                   BindingResult bindingResult) {
        System.out.println(form);
        Event event = eventService.getById(form.getEventId());
        if (!isCorrectPresentationTime(form)) {
            bindingResult.addError(new FieldError("form", "presentationStartTime",
                    "Incorrect time"));
        }
        if (bindingResult.hasErrors()) {
            return "presentation_add";
        }
        User author = userService.getByEmail(form.getAuthorEmail());
        Duration duration = Duration.ofMinutes(form.getDuration());
        PresentationStatus status = PresentationStatus.valueOf(form.getPresentationStatus());
        Presentation presentation = Presentation.builder()
                .author(author)
                .topic(form.getPresentationTopic())
                .description(form.getPresentationDescription())
                .startDate(LocalDateTime.of(form.getPresentationStartDate(), form.getPresentationStartTime()))
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
        Presentation presentation = event.getPresentationById(presentationId);

        PresentationForm form = new PresentationForm();
        form.setEventId(eventId);
        form.setPresentationId(presentationId);
        form.setPresentationTopic(presentation.getTopic());
        form.setPresentationDescription(presentation.getDescription());
        form.setPresentationStartDate(presentation.getStartDate().toLocalDate());
        form.setPresentationStartTime(presentation.getStartDate().toLocalTime());
        form.setDuration(presentation.getDuration().toMinutes());
        form.setPresentationStatus(presentation.getStatus().toString());
        form.setAuthorEmail(presentation.getAuthor().getEmail());
        modelAndView.addObject("form", form);
        return modelAndView;
    }

    @PostMapping("/presentation_edit")
    public String presentationEdit(@Valid @ModelAttribute("form") PresentationForm form, BindingResult bindingResult) {
        if (!isCorrectPresentationTime(form)) {
            bindingResult.addError(new FieldError("form", "presentationStartTime",
                    "Incorrect time"));
        }
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "presentation_edit";
        }
        User author = userService.getByEmail(form.getAuthorEmail());
        Event event = eventService.getById(form.getEventId());
        Presentation presentation = event.getPresentationById(form.getPresentationId());
        Duration duration = Duration.ofMinutes(form.getDuration());

        presentation.setAuthor(author);
        presentation.setTopic(form.getPresentationTopic());
        presentation.setDescription(form.getPresentationDescription());
        presentation.setStartDate(LocalDateTime.of(form.getPresentationStartDate(), form.getPresentationStartTime()));
        presentation.setDuration(duration);
        eventService.save(event);

        return "redirect:" + form.getEventId();
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
        presentationService.registerVisitor(presentationId, userId);
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
    public String sendMessages(@AuthenticationPrincipal User user,
                               @RequestParam Long eventId,
                               @RequestParam String subject,
                               @RequestParam String message) {
        Event event = eventService.getById(eventId);
        Set<User> users = event.getPresentations().stream()
                .filter((x) -> x.getStatus().equals(PresentationStatus.CONFIRMED))
                .map(Presentation::getRegistrations)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Message msg = Message.builder()
                .author(user)
                .recipients(users)
                .subject(subject)
                .text(message)
                .build();
        emailSenderService.sendMessages(msg);
        return "redirect:/management";
    }

    private boolean isCorrectPresentationTime(PresentationForm form) {
        Event event = eventService.getById(form.getEventId());
        LocalDateTime start = LocalDateTime.of(form.getPresentationStartDate(), form.getPresentationStartTime());
        LocalDateTime end = start.plus(Duration.ofMinutes(form.getDuration()));
        if (start.isBefore(event.getStartDate()) || end.isAfter(event.getFinishDate())) {
            return false;
        }
        List<Presentation> presentations = event.getPresentations();
        if (form.getPresentationId() != null) {
            Presentation presentation = event.getPresentationById(form.getPresentationId());
            presentations.remove(presentation);
        }
        return presentations.stream()
                .filter(x -> x.getStatus().equals(PresentationStatus.CONFIRMED))
                .filter(x -> !x.getFinishDateTime().isBefore(start))
                .filter(x -> !x.getStartDate().isAfter(end))
                .collect(Collectors.toSet()).isEmpty();
    }
}
