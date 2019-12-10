package ua.dmytrokashchenko.conferencesms.service.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dmytrokashchenko.conferencesms.domain.Bonus;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
class BonusUtilTest {
    private User user1;
    private User user2;
    private Map<User, Double> ratings;
    private Set<Bonus> bonuses;

    @BeforeEach
    public void setUp() {
        user1 = User.builder()
                .id(1L)
                .firstName("First1")
                .lastName("Last1")
                .email("email1@email.com")
                .password("password")
                .role(Role.SPEAKER)
                .build();
        user2 = User.builder()
                .id(2L)
                .firstName("First2")
                .lastName("Last2")
                .email("email2@email.com")
                .password("password")
                .role(Role.SPEAKER)
                .build();
        bonuses = new TreeSet<>();
        Bonus bonus1 = new Bonus(1L, 4.0, 80.0);
        Bonus bonus2 = new Bonus(2L, 3.0, 60.0);
        bonuses.add(bonus1);
        bonuses.add(bonus2);
        ratings = new HashMap<>();
        ratings.put(user1, 3.7);
        ratings.put(user2, 4.5);
    }

    @Test
    void shouldReturnCoefficientsForUsers() {
        Map<User, Double> actual = BonusUtil.getCoefficients(bonuses, ratings);
        Map<User, Double> expected = new HashMap<>();
        expected.put(user1, 60.0);
        expected.put(user2, 80.0);
        assertEquals(expected, actual);
    }
}