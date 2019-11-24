package ua.dmytrokashchenko.conferencesms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "eventEntity")
    private List<PresentationEntity> presentationEntities;
}
