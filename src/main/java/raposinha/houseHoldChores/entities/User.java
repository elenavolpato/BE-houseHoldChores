package raposinha.houseHoldChores.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(name="avatar_url")
    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

    public User( String username, String email, String password ) {
        this.username = username;
        this.email = email;
        this.password = password;
        // TODO: image will come from the frontend using the api directly there with a set of preset images. user will not be able to upload an image.
        this.avatarUrl = "https://res.cloudinary.com/dga90puif/image/upload/v1778151410/Screenshot_from_2026-05-07_12-53-38_tch5d6.png";
    }

}
