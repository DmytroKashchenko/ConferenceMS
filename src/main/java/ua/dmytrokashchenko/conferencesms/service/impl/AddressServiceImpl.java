package ua.dmytrokashchenko.conferencesms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.repository.AddressRepository;
import ua.dmytrokashchenko.conferencesms.service.AddressService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.AddressServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.AddressMapper;

@Log4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public void saveAddress(Address address) {
        if (address == null) {
            log.warn("Empty address");
            throw new AddressServiceException("Empty address");
        }
        addressRepository.save(addressMapper.mapAddressToEntity(address));
    }

    @Override
    public Address getAddressById(Long id) {
        if (id == null || id < 0) {
            log.warn("Invalid id");
            throw new AddressServiceException("Invalid id");
        }
        if (!addressRepository.findById(id).isPresent()) {
            log.info("No address with this id: " + id);
            throw new AddressServiceException("No address with this id");
        }
        return addressMapper.mapEntityToAddress(addressRepository.findById(id).get());
    }

    @Override
    public Page<Address> getAddresses(Pageable pageable) {
        return addressRepository.findAll(pageable).map(addressMapper::mapEntityToAddress);
    }
}
