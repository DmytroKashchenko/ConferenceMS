package ua.dmytrokashchenko.conferencesms.controller.unused;

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

public class AddressController {
    private static final Sort ORDER_FOR_ADDRESSES = Sort.by(Sort.Direction.DESC, "name");

    private final AddressService addressService;
    private final EventService eventService;

    public AddressController(AddressService addressService, EventService eventService) {
        this.addressService = addressService;
        this.eventService = eventService;
    }





    private Pageable getPageable(Integer pageNum, Integer pageSize) {
        pageNum = Math.max(pageNum, 1);
        pageSize = Math.max(pageSize, 1);
        pageNum--;
        return PageRequest.of(pageNum, pageSize, ORDER_FOR_ADDRESSES);
    }
}
