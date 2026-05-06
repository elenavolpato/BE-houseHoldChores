package raposinha.houseHoldChores.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="assigned_to")
    private User user;

    @Column(name="due_date")
    private LocalDateTime dueDate;

    // the frequency is converted to days on the FE
    private int frequency;

    @Column (name="is_completed")
    private boolean isCompleted;


}
