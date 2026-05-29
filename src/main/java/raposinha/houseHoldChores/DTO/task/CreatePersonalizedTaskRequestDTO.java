package raposinha.houseHoldChores.DTO.task;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreatePersonalizedTaskRequestDTO {

    @NotBlank(message = "Task title is required")
    private String title;

    @NotNull(message = "A category must be selected")
    private Long categoryId;

    private UUID assignedUserId;

    @NotNull(message = "Group ID is required")
    private Long groupId;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDateTime dueDate;

    @Min(value = 1, message = "Frequency must be at least 1 day")
    private int frequency;
}
