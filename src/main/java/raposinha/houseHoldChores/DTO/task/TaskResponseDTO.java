package raposinha.houseHoldChores.DTO.task;

import raposinha.houseHoldChores.entities.User;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponseDTO(
        Long taskId,
        String title,
        String categoryName,
        LocalDateTime dueDate,
        boolean isCompleted,
        UUID userID,
        String assignedTo,
        int frequency,
        String icon,
        String colorCode,
        String avatarUrl

) {}
