package raposinha.houseHoldChores.DTO;

import java.time.LocalDateTime;

public record SendEmailDTO(
        String message,
        LocalDateTime timestamp
) {


}
