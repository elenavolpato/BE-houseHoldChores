package raposinha.houseHoldChores.DTO.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Please provide a valid email address")
        String email
) {}