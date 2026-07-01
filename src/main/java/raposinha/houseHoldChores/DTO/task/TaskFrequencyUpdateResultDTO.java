package raposinha.houseHoldChores.DTO.task;

import java.util.List;

public record TaskFrequencyUpdateResultDTO(
        TaskResponseDTO parentTask,
        List<Long> deletedOccurrenceIds,
        List<TaskResponseDTO> newOccurrences
) {}