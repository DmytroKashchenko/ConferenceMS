package ua.dmytrokashchenko.conferencesms.domain;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
public class Presentation {
    private Long id;

    @NotNull
    private User author;

    @NotEmpty
    @Length(max = 255)
    private String topic;

    @NotEmpty
    @Length(max = 2000)
    private String description;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private Duration duration;

    @NotNull
    private PresentationStatus status;
    
    private Map<User, Boolean> registrations;
    private Map<User, Integer> ratings;

    public Map<User, Boolean> getRegistrations() {
        if (Objects.isNull(registrations)) {
            registrations = new HashMap<>();
        }
        return registrations;
    }

    public long getNumberOfVisitors() {
        return registrations.values().stream()
                .filter((x) -> x)
                .count();
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

    public LocalDateTime getFinishDateTime() {
        return startDate.plus(duration);
    }

}
