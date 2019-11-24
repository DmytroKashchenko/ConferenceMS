package ua.dmytrokashchenko.conferencesms.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.repository.AddressRepository;
import ua.dmytrokashchenko.conferencesms.service.AddressService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.AddressServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.AddressMapper;

@Service
public class AddressServiceImpl implements AddressService {
    private static final Logger LOGGER = Logger.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public Address addAddress(Address address) {
        if (address == null) {
            LOGGER.warn("Empty address");
            throw new AddressServiceException("Empty address");
        }
        return addressMapper.mapEntityToAddress(addressRepository.save(addressMapper.mapAddressToEntity(address)));
    }

    @Override
    public void editAddress(Address address) {
        if (address == null) {
            LOGGER.warn("Empty address");
            throw new AddressServiceException("Empty address");
        }
        addressRepository.save(addressMapper.mapAddressToEntity(address));
    }

    @Override
    public Address getAddressById(Long id) {
        if (id == null || id < 0) {
            LOGGER.warn("Invalid id");
            throw new AddressServiceException("Invalid id");
        }
        if (!addressRepository.findById(id).isPresent()) {
            LOGGER.warn("No address with this id: " + id);
            throw new AddressServiceException("No address with this id" + id);
        }
        return addressMapper.mapEntityToAddress(addressRepository.findById(id).get());
    }

    public Page<Address> getAddress(Integer pageNo, Integer pageSize, String sortBy) {
        if (pageNo == null || pageSize == null) {
            LOGGER.warn("Invalid page");
            throw new AddressServiceException("Invalid page");
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return addressRepository.findAll(paging).map(addressMapper::mapEntityToAddress);
    }
}
