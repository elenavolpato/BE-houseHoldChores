package raposinha.houseHoldChores.DTO.user;

import raposinha.houseHoldChores.entities.enums.GroupRole;
import java.util.UUID;

public record UserProfileResponseDTO(
        UUID id,
        String username,
        String email,
        String avatarUrl,
        Long groupId,         // Returns null if user hasn't joined a house yet
        String groupName,     // Returns null if user hasn't joined a house yet
        GroupRole role
){}
