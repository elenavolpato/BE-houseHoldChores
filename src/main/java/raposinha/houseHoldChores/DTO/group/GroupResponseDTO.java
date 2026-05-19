package raposinha.houseHoldChores.DTO.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raposinha.houseHoldChores.DTO.user.MemberOfGroupResponseDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationResponseDTO;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponseDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String groupName;

    @NotNull(message = "Every group must have an owner")
    private UUID ownerId;

    // returns the list of users so React can display the family members
    private List<MemberOfGroupResponseDTO> members;
}
