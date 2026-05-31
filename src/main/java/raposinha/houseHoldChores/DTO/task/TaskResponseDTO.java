package raposinha.houseHoldChores.DTO.task;

import raposinha.houseHoldChores.entities.User;
import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long taskId,
        String title,
        String categoryName,
        LocalDateTime dueDate,
        boolean isCompleted,
        String assignedTo,
        int frequency,
        String icon,
        String colorCode,
        String avatarUrl

) {}
