package raposinha.houseHoldChores.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.entities.enums.GroupRole;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberOfGroupResponseDTO {
    private UUID id;
    private String username;
    private String avatarUrl;
    private Group group;
    private GroupRole role;
    private String email;
}
