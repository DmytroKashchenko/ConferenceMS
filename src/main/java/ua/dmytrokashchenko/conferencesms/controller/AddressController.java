package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.domain.Event;
import ua.dmytrokashchenko.conferencesms.service.AddressService;
import ua.dmytrokashchenko.conferencesms.service.EventService;

@Controller
public class AddressController {
    private static final Sort ORDER_FOR_ADDRESSES = Sort.by(Sort.Direction.DESC, "name");

    private final AddressService addressService;
    private final EventService eventService;

    public AddressController(AddressService addressService, EventService eventService) {
        this.addressService = addressService;
        this.eventService = eventService;
    }

    @GetMapping("/address_management")
    public ModelAndView addresses(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = getPageable(pageNum, pageSize);
        Page<Address> addresses = addressService.getAddresses(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("address_management");
        modelAndView.addObject("addresses", addresses);
        return modelAndView;
    }

    @GetMapping("/address_add")
    public String addAddress() {
        return "address_add";
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

        return "redirect:/address_management";
    }

    @GetMapping("/address_edit")
    public ModelAndView addressEdit(@RequestParam(name = "addressId") Long addressId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("address_edit");
        Address address = addressService.getAddressById(addressId);
        modelAndView.addObject("address", address);
        return modelAndView;
    }

    @GetMapping("/address_selection")
    public ModelAndView addressSelection(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam Long eventId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("address_selection");
        Pageable pageable = getPageable(pageNum, pageSize);
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
        return "redirect:/event_management";
    }

    private Pageable getPageable(Integer pageNum, Integer pageSize) {
        pageNum = Math.max(pageNum, 1);
        pageSize = Math.max(pageSize, 1);
        pageNum--;
        return PageRequest.of(pageNum, pageSize, ORDER_FOR_ADDRESSES);
    }
}
