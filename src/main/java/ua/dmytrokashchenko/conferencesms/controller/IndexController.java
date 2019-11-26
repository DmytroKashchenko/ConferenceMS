package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.service.UserService;

import java.util.Map;

@Controller
public class IndexController {
    private UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo, Model model) {
        Page<User> users = userService.getUsers(pageNo, 10, "id");

        model.addAttribute("users", users);
        model.addAttribute("pageNo", pageNo);

        return "index";
    }

    @PostMapping
    public String addUser(@RequestParam String firstName,
                          @RequestParam String lastName,
                          @RequestParam String email,
                          @RequestParam String password,
                          Map<String, Object> model) {
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(Role.USER)
                .build();
        userService.register(user);
        Page<User> users = userService.getUsers(0, 10, "id");
        model.put("users", users);
        return "index";
    }

/*    @RequestMapping(value={"/"}, method = RequestMethod.GET)
    public ModelAndView users(){
        Page<User> users = userService.getUsers(0, 10, "id");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("index");
        return modelAndView;
    }*/
}
