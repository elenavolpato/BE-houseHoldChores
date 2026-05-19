package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    // weekly calendar: fetches all tasks for a group between two dates
    List<Task> findByAssignedToGroup_IdAndDueDateBetween(Long groupId, LocalDateTime start, LocalDateTime end);

    // only the tasks assigned to a specific person
    List<Task> findByAssignedToId(UUID userId);

    // tasks that are overdue and not completed
    List<Task> findByIsCompletedFalseAndDueDateBefore(LocalDateTime now);
}