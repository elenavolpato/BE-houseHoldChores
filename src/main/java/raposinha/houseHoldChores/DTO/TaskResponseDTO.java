package raposinha.houseHoldChores.DTO;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long taskId,
        String title,
        String categoryName,
        LocalDateTime dueDate,
        boolean isCompleted,
        String assignedTo,
        int frequency
) {}
