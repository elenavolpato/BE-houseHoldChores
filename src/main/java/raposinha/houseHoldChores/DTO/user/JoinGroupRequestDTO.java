package raposinha.houseHoldChores.DTO.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JoinGroupRequestDTO(
        @NotBlank(message = "The group invitation code cannot be empty")
        @Size(min = 6, max = 12, message = "Invitation codes must be between 6 and 12 characters long")
        String inviteCode
) {}
