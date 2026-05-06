package raposinha.houseHoldChores.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // this represents the ADMIN/OWNER (1-to-1)
    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User owner;

    // get all members
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<User> members;
}
