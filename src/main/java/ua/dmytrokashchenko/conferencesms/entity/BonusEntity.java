package ua.dmytrokashchenko.conferencesms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bonuses")
public class BonusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bonus_id")
    private Long id;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "coefficient")
    private Double coefficient;
}
