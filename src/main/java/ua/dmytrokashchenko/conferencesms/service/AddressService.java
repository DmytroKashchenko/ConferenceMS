package ua.dmytrokashchenko.conferencesms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.dmytrokashchenko.conferencesms.domain.Address;

public interface AddressService {
    Address addAddress(Address address);

    void saveAddress(Address address);

    Address getAddressById(Long id);

    Page<Address> getAddresses(Pageable pageable);
}
