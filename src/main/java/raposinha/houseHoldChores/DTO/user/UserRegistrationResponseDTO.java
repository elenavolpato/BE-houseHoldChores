package raposinha.houseHoldChores.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String avatarUrl;
    private Long groupId;
}