package raposinha.houseHoldChores.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTaskFromPresetDTO(
        Long presetId,
        UUID assignedUserId,
        String groupId,
        LocalDateTime dueDate,
        int frequency
) {}
