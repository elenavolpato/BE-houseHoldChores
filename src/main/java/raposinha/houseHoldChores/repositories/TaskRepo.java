package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Modifying // Required for queries modifying the database state
    @Query("UPDATE Task t SET t.assignedTo = null WHERE t.assignedTo.id = :userId")
    void unassignTasksByUserId(@Param("userId") UUID userId);
}