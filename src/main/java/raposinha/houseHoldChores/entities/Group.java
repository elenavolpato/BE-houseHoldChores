package raposinha.houseHoldChores.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Group {

    @Id
    private String id;

    @Column (name="group_name")
    private String groupName;

    // this represents the ADMIN/OWNER (1-to-1)
    // it's nullable true in order do avoid circular reference.
    @OneToOne
    @JoinColumn(name="admin_id", referencedColumnName = "id", nullable = true)
    private User owner;

    // get all members | initializes the array as
    @OneToMany(mappedBy = "group")
    private List<User> members = new ArrayList<>();
}
