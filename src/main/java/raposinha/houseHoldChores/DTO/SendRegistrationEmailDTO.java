package raposinha.houseHoldChores.DTO;

import java.time.LocalDateTime;

public record SendRegistrationEmailDTO(
        String message,
        LocalDateTime timestamp
) {

}
