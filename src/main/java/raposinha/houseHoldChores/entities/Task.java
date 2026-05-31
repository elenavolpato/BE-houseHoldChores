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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_gen")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", nullable = true)
    private User assignedTo;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "avatar_url")
    private String avatarUrl;


}
