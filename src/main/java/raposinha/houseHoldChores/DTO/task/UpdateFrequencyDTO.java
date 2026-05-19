package raposinha.houseHoldChores.DTO.task;

import jakarta.validation.constraints.Min;

public record UpdateFrequencyDTO(
        @Min(value = 1, message = "Frequency must be at least 1 day")
        int frequency
) {}