package raposinha.houseHoldChores.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UpdateDueDateDTO(
        @NotNull(message = "Due date is required")
        @FutureOrPresent(message = "Due date cannot be in the past")
        LocalDateTime dueDate
) {}