package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.data.domain.Page;
import ua.dmytrokashchenko.conferencesms.domain.Address;

public interface AddressService {
    Address addAddress(Address address);

    void editAddress(Address address);

    Address getAddressById(Long id);

    Page<Address> getAddress(Integer pageNo, Integer pageSize, String sortBy);
}
