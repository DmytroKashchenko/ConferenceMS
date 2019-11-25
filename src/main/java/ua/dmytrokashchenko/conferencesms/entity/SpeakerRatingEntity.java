package ua.dmytrokashchenko.conferencesms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Map;

/*@Data
@NoArgsConstructor
@Entity*/
public class SpeakerRatingEntity {

    private UserEntity user; // якому спікеру належать оцінки

    private PresentationEntity presentation; // яку презентацію оцінюють

    private Map<UserEntity, Integer> ratings; // які відвідувачі які оцінки поставили
}
