package raposinha.houseHoldChores.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;


@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name="avatar_url")
    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

    public User( String username, String email ,String avatarUrl) {
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }
}
