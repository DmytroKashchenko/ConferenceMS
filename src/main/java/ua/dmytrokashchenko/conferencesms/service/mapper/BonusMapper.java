package ua.dmytrokashchenko.conferencesms.service.mapper;

import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Bonus;
import ua.dmytrokashchenko.conferencesms.entity.BonusEntity;

@Component
public class BonusMapper {
    public Bonus mapEntityToBonus(BonusEntity bonusEntity) {
        return new Bonus(bonusEntity.getId(), bonusEntity.getRating(), bonusEntity.getCoefficient());
    }

    public BonusEntity mapBonusToEntity(Bonus bonus) {
        BonusEntity bonusEntity = new BonusEntity();
        bonusEntity.setId(bonus.getId());
        bonusEntity.setRating(bonus.getRating());
        bonusEntity.setCoefficient(bonus.getCoefficient());
        return bonusEntity;
    }
}
