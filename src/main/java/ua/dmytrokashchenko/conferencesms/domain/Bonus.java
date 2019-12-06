package ua.dmytrokashchenko.conferencesms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Bonus implements Comparable<Bonus> {
    private Long id;

    @NotEmpty
    @Min(value = 0, message = "Rating should not be less than 0")
    @Max(value = 5, message = "Rating should not be greater than 5")
    private Double rating;

    @NotEmpty
    @Min(value = 0, message = "Coefficient should not be less than 0")
    @Max(value = 100, message = "Coefficient should not be greater than 100")
    private Double coefficient;

    @Override
    public int compareTo(Bonus o) {
        return -this.rating.compareTo(o.rating);
    }
}
