package raposinha.houseHoldChores.DTO;

import java.time.LocalDateTime;

public record ExceptionDTO(
        String message,
        int status,
        LocalDateTime timestamp
) {
}
