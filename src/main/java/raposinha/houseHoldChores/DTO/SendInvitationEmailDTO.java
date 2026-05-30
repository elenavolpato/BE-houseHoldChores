package raposinha.houseHoldChores.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendInvitationEmailDTO(
        @NotBlank(message = "Recipient name is required")
        String recipientName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String recipientEmail

) {}
