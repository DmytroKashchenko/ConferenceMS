package ua.dmytrokashchenko.conferencesms.service.util;

import lombok.extern.log4j.Log4j;
import ua.dmytrokashchenko.conferencesms.domain.Bonus;
import ua.dmytrokashchenko.conferencesms.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j
public class BonusUtil {
    private BonusUtil() {
    }

    public static Map<User, Double> getCoefficients(Set<Bonus> bonuses, Map<User, Double> ratings) {
        if (bonuses == null || ratings == null) {
            log.warn("Illegal arguments: bonuses or ratings");
            throw new IllegalArgumentException("Illegal arguments: bonuses or ratings");
        }
        Map<User, Double> coefficients = new HashMap<>();
        Set<User> users = ratings.keySet();
        for (User user : users) {
            coefficients.put(user, 0.0);
            Double rating = ratings.get(user);
            if (rating == null) {
                continue;
            }
            for (Bonus bonus : bonuses) {
                if (bonus.getRating() > rating) {
                    continue;
                }
                coefficients.put(user, bonus.getCoefficient());
                break;
            }
        }
        return coefficients;
    }
}
