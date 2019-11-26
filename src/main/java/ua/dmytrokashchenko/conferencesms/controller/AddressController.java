package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.service.AddressService;

@Controller
public class AddressController {
    private static final Sort ORDER_FOR_ADDRESSES = Sort.by("id");

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/addresses")
    public ModelAndView addresses(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = getPageable(pageNum, pageSize);
        Page<Address> addresses = addressService.getAddresses(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addresses");
        modelAndView.addObject("addresses", addresses);
        return modelAndView;
    }


    private Pageable getPageable(Integer pageNum, Integer pageSize) {
        pageNum = Math.max(pageNum, 1);
        pageSize = Math.max(pageSize, 1);
        pageNum--;
        return PageRequest.of(pageNum, pageSize, ORDER_FOR_ADDRESSES);
    }
}
