package raposinha.houseHoldChores.DTO;

import java.time.LocalDateTime;

public record ExceptionDTO(
        String message,
        LocalDateTime timestamp
) {
}
