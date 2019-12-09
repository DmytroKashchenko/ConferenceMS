package ua.dmytrokashchenko.conferencesms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.service.PresentationService;
import ua.dmytrokashchenko.conferencesms.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PresentationService presentationService;

    @GetMapping("/registration")
    public String registration(User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid User user, BindingResult bindingResult) {
        if (userService.isRegistered(user)) {
            bindingResult.addError(new FieldError("user", "email",
                    "User with this email already registered"));
        }
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        user.setRole(Role.USER);
        userService.register(user);
        return "redirect:/login";
    }

    @PostMapping("/user/register_for_presentation")
    public String registerForPresentation(@AuthenticationPrincipal User user,
                                          @RequestParam Long presentationId,
                                          @RequestParam Long eventId) {
        presentationService.registerForPresentation(presentationId, user);
        return "redirect:/events/upcoming/" + eventId;
    }

    @PostMapping("/rate_presentation")
    public String ratePresentation(@AuthenticationPrincipal User user,
                                   @RequestParam Integer rating,
                                   @RequestParam Long presentationId,
                                   @RequestParam Long eventId) {
        presentationService.ratePresentation(user, rating, presentationId);
        return "redirect:/events/past/" + eventId;
    }
}
