package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.entity.*;
import ua.dmytrokashchenko.conferencesms.repository.AddressRepository;
import ua.dmytrokashchenko.conferencesms.repository.EventRepository;
import ua.dmytrokashchenko.conferencesms.repository.UserRepository;
import ua.dmytrokashchenko.conferencesms.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    private UserService userService;

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private AddressRepository addressRepository;

    public IndexController(UserService userService, EventRepository eventRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo, Model model) {
        Page<User> users = userService.getUsers(pageNo, 10, "id");

        model.addAttribute("users", users);
        model.addAttribute("pageNo", pageNo);

        //#####################################################################

//        AddressEntity addressEntity = new AddressEntity();
//        addressEntity.setName("Test address");
//        addressEntity.setCountry("Test Country");
//        addressEntity.setCity("Test city");
//        addressEntity.setDetailedAddress("detailed address");
//
//        addressRepository.save(addressEntity);
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setFirstName("First");
//        userEntity.setLastName("Last");
//        userEntity.setRoleEntity(RoleEntity.SPEAKER);
//        userEntity.setEmail("testemail@email.com");
//        userEntity.setPassword("password");
//        userRepository.save(userEntity);


//        AddressEntity addressEntity = addressRepository.findById(1L).orElseThrow(NullPointerException::new);
//
//
//        EventEntity eventEntity = new EventEntity();
//        eventEntity.setAddressEntity(addressEntity);
//        eventEntity.setName("Test event name");
//        eventEntity.setStartDate(LocalDateTime.now());
//        eventEntity.setFinishDate(LocalDateTime.now());
//        eventEntity.setEventDetails("Test event details");


        EventEntity eventEntity = eventRepository.findById(1L).orElseThrow(NullPointerException::new);
//
//        UserEntity userEntity = userRepository.findById(1L).orElseThrow(NullPointerException::new);
//
//        PresentationEntity presentationEntity1 = new PresentationEntity();
//        presentationEntity1.setAuthor(userEntity);
//        presentationEntity1.setStartDate(LocalDateTime.now());
//        presentationEntity1.setDuration(Duration.ofMinutes(10));
//        presentationEntity1.setTopic("Test topic1");
//        presentationEntity1.setDescription("test description1");
//        presentationEntity1.setStatus(PresentationStatusEntity.CONFIRMED);
//
//        PresentationEntity presentationEntity2 = new PresentationEntity();
//        presentationEntity2.setAuthor(userEntity);
//        presentationEntity2.setStartDate(LocalDateTime.now());
//        presentationEntity2.setDuration(Duration.ofMinutes(15));
//        presentationEntity2.setTopic("Test topic2");
//        presentationEntity2.setDescription("test description2");
//        presentationEntity2.setStatus(PresentationStatusEntity.CONFIRMED);
//
//        List<PresentationEntity> presentationEntities = new ArrayList<>();
//        presentationEntities.add(presentationEntity1);
//        presentationEntities.add(presentationEntity2);
//
//        eventEntity.setPresentationEntities(presentationEntities);
//
//
//        eventRepository.save(eventEntity);

        for (PresentationEntity presentationEntity: eventEntity.getPresentationEntities()) {
            System.out.println(presentationEntity.getId());
        }


        //######################################################################

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
