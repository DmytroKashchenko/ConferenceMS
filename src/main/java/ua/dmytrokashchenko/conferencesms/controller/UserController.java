package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.domain.Presentation;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.service.EventService;
import ua.dmytrokashchenko.conferencesms.service.UserService;

@Controller
public class UserController {
    private final UserService userService;
    private final EventService eventService;

    public UserController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String email,
                               @RequestParam String password) {

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(Role.USER)
                .build();

        userService.register(user);

        return "redirect:/login";
    }

    @PostMapping("/user/register_for_presentation")
    public String registerForPresentation(@AuthenticationPrincipal User user,
                                          @RequestParam Long presentationId) {
        Event event = eventService.getEventByPresentationId(presentationId);
        Presentation presentation = event.getPresentationById(presentationId);
        if (presentation.getRegistrations().get(user) != null) {
            return "redirect:/events/upcoming/" + event.getId(); //TODO need to add processing?
        }
        presentation.addRegistration(user);
        eventService.save(event);
        return "redirect:/events/upcoming/" + event.getId();
    }

    @PostMapping("/rate_presentation")
    public String ratePresentation(@AuthenticationPrincipal User user,
                                   @RequestParam Integer rating,
                                   @RequestParam Long presentationId) {
        Event event = eventService.getEventByPresentationId(presentationId);
        Presentation presentation = event.getPresentationById(presentationId);
        if (presentation.getRegistrations().get(user) == null || !presentation.getRegistrations().get(user)) {
            return "redirect:/"; // TODO need to add processing
        }
        presentation.addUserRating(user, rating);
        eventService.save(event);
        return "redirect:/events/past/" + event.getId();
    }

}
