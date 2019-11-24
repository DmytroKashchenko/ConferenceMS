package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.entity.AddressEntity;

@Component
public class AddressMapper {

    public Address mapEntityToAddress(AddressEntity addressEntity) {
        return Address.builder()
                .id(addressEntity.getId())
                .name(addressEntity.getName())
                .country(addressEntity.getCountry())
                .city(addressEntity.getCity())
                .detailedAddress(addressEntity.getDetailedAddress())
                .build();
    }

    public AddressEntity mapAddressToEntity(Address address) {
        return new AddressEntity(address.getId(), address.getName(), address.getCountry(),
                address.getCity(), address.getDetailedAddress());
    }
}
