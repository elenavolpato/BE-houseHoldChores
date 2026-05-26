package raposinha.houseHoldChores.DTO.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateAvatarRequestDTO(
        @NotBlank(message = "Avatar URL string cannot be blank")
        @Pattern(regexp = "^https://.*", message = "Avatar must be a valid secured URL string")
        String avatarUrl
) {}
