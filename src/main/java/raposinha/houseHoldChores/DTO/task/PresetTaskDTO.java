package raposinha.houseHoldChores.DTO.task;

public record PresetTaskDTO (
        Long categoryId,
        Long taskId,
        String title,
        int frequency,
        Long groupId
    ) {}

