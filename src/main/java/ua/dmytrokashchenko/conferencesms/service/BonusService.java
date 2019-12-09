package ua.dmytrokashchenko.conferencesms.service;

import ua.dmytrokashchenko.conferencesms.domain.Bonus;

import java.util.Set;

public interface BonusService {

    void saveRecord(Bonus bonus);

    void deleteById(Long id);

    Set<Bonus> getRecords();
}
