package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.domain.*;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.PresentationService;
import ua.dmytrokashchenko.conferencesms.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/speaker")
public class SpeakerController {
    private final PresentationService presentationService;
    private final EventService eventService;
    private final UserService userService;

    public SpeakerController(PresentationService presentationService, EventService eventService,
                             UserService userService) {
        this.presentationService = presentationService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/suggested_by_moderator")
    public ModelAndView showSuggestedPresentations(@AuthenticationPrincipal User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("suggested_by_moderator");
        List<Presentation> presentations =
                presentationService.getPresentationsByAuthorAndStatus(user, PresentationStatus.SUGGESTED_BY_MODERATOR);
        modelAndView.addObject("presentations", presentations);

        return modelAndView;
    }

    @PostMapping("/confirm_presentation")
    public String confirmPresentation(@RequestParam Long presentationId) {
        Event event = eventService.getEventByPresentationId(presentationId);
        Presentation presentation = event.getPresentationById(presentationId);
        presentation.setStatus(PresentationStatus.CONFIRMED);
        eventService.save(event);
        return "redirect:/speaker/suggested_by_moderator";
    }

    @GetMapping("/suggest_presentation")
    public ModelAndView suggestPresentation(@RequestParam Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("suggest_presentation");
        modelAndView.addObject("eventId", eventId);
        return modelAndView;
    }

    @PostMapping("/suggest_presentation")
    public String suggestPresentation(@AuthenticationPrincipal User user,
                                      @RequestParam String presentationTopic,
                                      @RequestParam String presentationDescription,
                                      @RequestParam
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                              LocalDateTime presentationStart,
                                      @RequestParam Integer presentationDuration,
                                      @RequestParam Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("suggest_presentation");
        Event event = eventService.getById(eventId);

        Duration duration = Duration.ofMinutes(presentationDuration);
        Presentation presentation = Presentation.builder()
                .author(user)
                .topic(presentationTopic)
                .description(presentationDescription)
                .startDate(presentationStart)
                .duration(duration)
                .status(PresentationStatus.SUGGESTED_BY_SPEAKER)
                .build();
        event.addPresentation(presentation);
        eventService.save(event);

        return "redirect:/";
    }

    public ModelAndView showSpeakerRating(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("show_speaker_rating");
        Page<User> speakers = userService.getUsersByRole(Role.SPEAKER, pageable);

        return modelAndView;
    }


}
