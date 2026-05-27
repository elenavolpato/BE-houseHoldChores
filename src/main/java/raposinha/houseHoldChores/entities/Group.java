package raposinha.houseHoldChores.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
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

    @Column (name="group_name")
    private String groupName;

    // it's nullable true in order do avoid circular reference.
    @ManyToOne
    @JoinColumn(name="admin_id", referencedColumnName = "id", nullable = true)
    @ToString.Exclude
    private User owner;

    // get all members | initializes the array
    @OneToMany(mappedBy = "group")
    @JsonManagedReference
    @ToString.Exclude
    private List<User> members = new ArrayList<>();


}
