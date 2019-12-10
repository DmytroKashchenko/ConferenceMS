package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Bonus;
import ua.dmytrokashchenko.conferencesms.entity.BonusEntity;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BonusMapperTest {
    private Bonus bonus;
    private BonusEntity bonusEntity;

    private BonusMapper bonusMapper = new BonusMapper();

    @Before
    public void setUp() {
        bonusEntity = new BonusEntity();
        bonusEntity.setId(1L);
        bonusEntity.setRating(5.0);
        bonusEntity.setCoefficient(100.0);
        bonus = new Bonus(1L, 5.0, 100.0);
    }

    @Test
    public void shouldSuccessfullyMapEntityToBonus() {
        Bonus expected = bonus;
        Bonus actual = bonusMapper.mapEntityToBonus(bonusEntity);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSuccessfullyMapBonusToEntity() {
        BonusEntity expected = bonusEntity;
        BonusEntity actual = bonusMapper.mapBonusToEntity(bonus);
        assertEquals(expected, actual);
    }
}