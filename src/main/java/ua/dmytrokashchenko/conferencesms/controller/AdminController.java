package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.domain.Bonus;
import ua.dmytrokashchenko.conferencesms.domain.Message;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.service.BonusService;
import ua.dmytrokashchenko.conferencesms.service.EmailSenderService;
import ua.dmytrokashchenko.conferencesms.service.UserService;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final BonusService bonusService;
    private final EmailSenderService emailSenderService;

    public AdminController(UserService userService, BonusService bonusService,
                           EmailSenderService emailSenderService) {
        this.userService = userService;
        this.bonusService = bonusService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/users")
    public ModelAndView showUsers(
            @PageableDefault(sort = "roleEntity", direction = Sort.Direction.ASC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users_management");
        Page<User> users = userService.getUsers(pageable);
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @PostMapping("/set_role")
    public String setUserRole(@RequestParam Long userId, @RequestParam String roleName) {
        User user = userService.getById(userId);
        Role role = Role.valueOf(roleName);
        user.setRole(role);
        userService.update(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/bonuses")
    public ModelAndView showBonusesTable() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("bonuses_management");
        Set<Bonus> bonuses = bonusService.getRecords();
        modelAndView.addObject("bonuses", bonuses);
        return modelAndView;
    }

    @PostMapping("/bonus_delete")
    public String deleteBonus(@RequestParam Long bonusId) {
        bonusService.deleteById(bonusId);
        return "redirect:/admin/bonuses";
    }

    @PostMapping("/bonus_add")
    public String addBonus(@RequestParam Double rating,
                           @RequestParam Double coefficient) {
        Bonus bonus = new Bonus();
        bonus.setRating(rating);
        bonus.setCoefficient(coefficient);
        bonusService.saveRecord(bonus);
        return "redirect:/admin/bonuses";
    }

    @GetMapping("/sent_messages")
    public ModelAndView showSentMessages(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Message> messages = emailSenderService.getMessages(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sent_messages");
        modelAndView.addObject("messages", messages);
        return modelAndView;
    }
}
