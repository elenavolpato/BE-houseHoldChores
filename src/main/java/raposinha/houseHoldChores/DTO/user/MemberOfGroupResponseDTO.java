package raposinha.houseHoldChores.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberOfGroupResponseDTO {
    private UUID id;
    private String username;
    private Long groupId;
}
