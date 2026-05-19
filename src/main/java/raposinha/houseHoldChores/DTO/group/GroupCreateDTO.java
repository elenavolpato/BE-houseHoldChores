package raposinha.houseHoldChores.DTO.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GroupCreateDTO {

    @NotBlank(message = "Group name cannot be empty")
    @Size(min = 2, max = 50, message = "Group name must be between 2 and 50 characters")
    private String groupName;
}
