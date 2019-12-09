package ua.dmytrokashchenko.conferencesms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Address")
@Table(name = "addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "address_name")
    private String name;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "details")
    private String detailedAddress;
}
