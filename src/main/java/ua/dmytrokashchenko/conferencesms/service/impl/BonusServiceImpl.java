package ua.dmytrokashchenko.conferencesms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ua.dmytrokashchenko.conferencesms.domain.Bonus;
import ua.dmytrokashchenko.conferencesms.repository.BonusRepository;
import ua.dmytrokashchenko.conferencesms.service.BonusService;
import ua.dmytrokashchenko.conferencesms.service.exceptions.BonusServiceException;
import ua.dmytrokashchenko.conferencesms.service.mapper.BonusMapper;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Log4j
@Service
@RequiredArgsConstructor
public class BonusServiceImpl implements BonusService {
    private final BonusRepository bonusRepository;
    private final BonusMapper bonusMapper;

    @Override
    public void saveRecord(Bonus bonus) {
        if (bonus == null) {
            log.warn("invalid bonus object");
            throw new BonusServiceException("invalid bonus object");
        }
        bonusRepository.save(bonusMapper.mapBonusToEntity(bonus));
    }

    @Override
    public void deleteById(Long id) {
        if (id == null || id < 0) {
            log.warn("invalid id");
            throw new BonusServiceException("invalid id");
        }
        if (!bonusRepository.findById(id).isPresent()) {
            log.info("No record with this id" + id);
            throw new BonusServiceException("No record with this id" + id);
        }
        bonusRepository.deleteById(id);
    }

    @Override
    public Set<Bonus> getRecords() {
        return bonusRepository.findAll().stream()
                .map(bonusMapper::mapEntityToBonus)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
