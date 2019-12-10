package ua.dmytrokashchenko.conferencesms.service.impl;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Bonus;
import ua.dmytrokashchenko.conferencesms.entity.BonusEntity;
import ua.dmytrokashchenko.conferencesms.repository.BonusRepository;
import ua.dmytrokashchenko.conferencesms.service.exceptions.BonusServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.BonusMapper;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BonusServiceImplTest {
    private Bonus bonus;
    private BonusEntity bonusEntity;

    @MockBean
    private BonusRepository bonusRepository;

    @MockBean
    private BonusMapper bonusMapper;

    @InjectMocks
    @Resource
    private BonusServiceImpl bonusService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        bonus = new Bonus(1L, 4.0, 80.0);
        bonusEntity = new BonusEntity();
        bonusEntity.setId(1L);
        bonusEntity.setRating(4.0);
        bonusEntity.setCoefficient(80.0);
    }

    @After
    public void resetMocks() {
        reset(bonusMapper, bonusRepository);
    }

    @Test
    public void shouldSuccessfullySaveBonus() {
        when(bonusMapper.mapBonusToEntity(bonus)).thenReturn(bonusEntity);
        when(bonusRepository.save(bonusEntity)).thenReturn(bonusEntity);

        bonusService.saveRecord(bonus);

        verify(bonusRepository).save(bonusEntity);
    }

    @Test
    public void shouldSuccessfullyDeleteById() {
        when(bonusRepository.findById(1L)).thenReturn(Optional.of(bonusEntity));
        doNothing().when(bonusRepository).deleteById(1L);

        bonusService.deleteById(1L);

        verify(bonusRepository).deleteById(1L);
    }

    @Test
    public void shouldThrowExceptionWhenBonusNotExists() {
        exception.expect(BonusServiceException.class);
        exception.expectMessage("No record with this id");

        when(bonusRepository.findById(1L)).thenReturn(Optional.empty());

        bonusService.deleteById(1L);
    }

    @Test
    public void shouldSuccessfullyGetRecords() {
        when(bonusRepository.findAll()).thenReturn(Arrays.asList(bonusEntity));
        when(bonusMapper.mapEntityToBonus(bonusEntity)).thenReturn(bonus);

        Set<Bonus> expected = new TreeSet<>();
        expected.add(bonus);
        Set<Bonus> actual = bonusService.getRecords();

        Assert.assertEquals(expected, actual);
    }
}
