package raposinha.houseHoldChores.DTO.task;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTaskFromPresetDTO(
        Long presetId,
        UUID assignedUserId,
        Long groupId,
        LocalDateTime dueDate,
        int frequency
) {}
