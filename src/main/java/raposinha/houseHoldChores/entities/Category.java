package raposinha.houseHoldChores.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private String icon; // Optional: e.g., "fa-broom" or a URL

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    @JsonManagedReference
    private List<Task> tasks;

    public Category(String name) {
        this.name = name;
    }
}
