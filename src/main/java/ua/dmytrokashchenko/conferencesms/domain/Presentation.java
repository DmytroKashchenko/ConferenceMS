package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
public class Presentation {
    private Long id;
    private User author;
    private String topic;
    private String description;
    private LocalDateTime startDate;
    private Duration duration;
    private PresentationStatus status;
    private Map<User, Boolean> registrations;
    private Map<User, Integer> ratings;

    public Map<User, Boolean> getRegistrations() {
        if (Objects.isNull(registrations)) {
            registrations = new HashMap<>();
        }
        return registrations;
    }

    public Map<User, Integer> getRatings() {
        if (Objects.isNull(ratings)) {
            ratings = new HashMap<>();
        }
        return ratings;
    }

    public void addRegistration(User user) {
        if (Objects.isNull(registrations)) {
            registrations = new HashMap<>();
        }
        registrations.put(user, false);
    }

    public void addUserRating(User user, Integer rating) {
        if (ratings == null) {
            ratings = new HashMap<>();
        }
        ratings.put(user, rating);
    }

}
