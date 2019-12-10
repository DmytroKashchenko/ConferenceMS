package ua.dmytrokashchenko.conferencesms.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Address;
import ua.dmytrokashchenko.conferencesms.entity.AddressEntity;
import ua.dmytrokashchenko.conferencesms.repository.AddressRepository;
import ua.dmytrokashchenko.conferencesms.service.exceptions.AddressServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.AddressMapper;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressServiceImplTest {
    private Address address;
    private AddressEntity addressEntity;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private AddressMapper addressMapper;

    @InjectMocks
    @Resource
    private AddressServiceImpl addressService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        address = Address.builder()
                .id(1L)
                .name("Test")
                .country("Test")
                .city("Test")
                .detailedAddress("Test")
                .build();
        addressEntity = new AddressEntity();
    }

    @After
    public void resetMocks() {
        reset(addressRepository, addressMapper);
    }

    @Test
    public void shouldSuccessfullySaveAddress() {
        when(addressMapper.mapAddressToEntity(address)).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);

        addressService.saveAddress(address);

        verify(addressRepository).save(addressEntity);
    }

    @Test
    public void shouldSuccessfullyGetAddressById() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(addressEntity));
        when(addressMapper.mapEntityToAddress(addressEntity)).thenReturn(address);

        Address actual = addressService.getAddressById(1L);
        Address expected = address;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenAddressNotExists() {
        exception.expect(AddressServiceException.class);
        exception.expectMessage("No address with this id");

        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        addressService.getAddressById(1L);
    }

    @Test
    public void shouldReturnAddressesPage() {
        Pageable pageable = Pageable.unpaged();
        Page<AddressEntity> addressEntities = new PageImpl<>(Arrays.asList(addressEntity, addressEntity));

        when(addressMapper.mapEntityToAddress(addressEntity)).thenReturn(address);
        when(addressRepository.findAll(pageable)).thenReturn(addressEntities);

        Page<Address> actual = addressService.getAddresses(pageable);
        Page<Address> expected = new PageImpl<>(Arrays.asList(address, address));

        assertEquals(expected, actual);
    }
}