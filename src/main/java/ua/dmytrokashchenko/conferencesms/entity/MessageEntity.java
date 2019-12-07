package ua.dmytrokashchenko.conferencesms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "message_recipients",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> recipients;

    @Column(name = "message_subject")
    private String subject;

    @Column(name = "message_text", length = 10000)
    private String text;
}
