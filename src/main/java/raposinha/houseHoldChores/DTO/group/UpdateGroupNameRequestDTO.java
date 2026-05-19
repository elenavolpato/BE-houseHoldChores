package raposinha.houseHoldChores.DTO.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateGroupNameRequestDTO(
        @NotBlank(message = "The group name cannot be blank")
        @Size(max = 255, message = "The group name must be less than 255 characters")
        String newGroupName
) {}