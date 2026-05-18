package raposinha.houseHoldChores.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id")
    @SequenceGenerator(
            name = "task_id_gen",
            sequenceName = "tasks_id_seq",
            initialValue = 1000,
            allocationSize = 1
    )
    @Column(name="task_id")
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    private int frequency;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User user;

    @Column(name = "is_completed")
    private boolean isCompleted;


    public void setId(Long id) {
        this.id = 1000 + id;
    }
}
