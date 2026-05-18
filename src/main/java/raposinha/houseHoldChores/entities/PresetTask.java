package raposinha.houseHoldChores.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "preset_tasks")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PresetTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // the frequency is converted to days on the FE
    private int frequency;

    @ManyToOne
    // NULL means it's a "Global" preset
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;


}