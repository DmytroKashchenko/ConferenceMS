package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.entity.AddressEntity;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressMapperTest {
    private final AddressMapper addressMapper = new AddressMapper();

    private Address address;
    private AddressEntity addressEntity;

    @Before
    public void setUp() {
        address = Address.builder()
                .city("CityName")
                .name("AddressName")
                .country("Country")
                .detailedAddress("Address details")
                .id(100L)
                .build();

        addressEntity = new AddressEntity();
        addressEntity.setId(100L);
        addressEntity.setName("AddressName");
        addressEntity.setCountry("Country");
        addressEntity.setCity("CityName");
        addressEntity.setDetailedAddress("Address details");
    }

    @Test
    public void shouldSuccessfullyMapEntityToAddress() {
        Address actual = addressMapper.mapEntityToAddress(addressEntity);
        Address expected = address;

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getDetailedAddress(), actual.getDetailedAddress());

    }

    @Test
    public void shouldSuccessfullyMapAddressToEntity() {
        AddressEntity actual = addressMapper.mapAddressToEntity(address);
        AddressEntity expected = addressEntity;

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getDetailedAddress(), actual.getDetailedAddress());

    }
}