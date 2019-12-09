package ua.dmytrokashchenko.conferencesms.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Presentation")
@Table(name = "presentations")
public class PresentationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presentation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;

    @Column(name = "topic")
    private String topic;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "start_date_time")
    private LocalDateTime startDate;

    @Column(name = "duration")
    private Duration duration;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PresentationStatusEntity status;

    @ElementCollection
    @CollectionTable(name = "presentation_registrations")
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "is_visitor")
    private Map<UserEntity, Boolean> registrations;

    @ElementCollection
    @CollectionTable(name = "user_ratings")
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "rating")
    private Map<UserEntity, Integer> ratings;
}
