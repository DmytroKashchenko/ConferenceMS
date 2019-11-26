package ua.dmytrokashchenko.conferencesms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Event")
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_name")
    private String name;

    @Column(name = "event_details")
    private String eventDetails;

    @Column(name = "start")
    private LocalDateTime startDate;

    @Column(name = "finish")
    private LocalDateTime finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity addressEntity;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "event_presentations",
            joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "presentation_id",
                    referencedColumnName = "presentation_id", unique = true)})
    private List<PresentationEntity> presentationEntities;

}
