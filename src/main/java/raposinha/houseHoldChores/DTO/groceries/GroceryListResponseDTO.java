package raposinha.houseHoldChores.DTO.groceries;

import java.time.LocalDateTime;

public record GroceryListResponseDTO(
        Long id,
        String name,
        Long groupId,
        LocalDateTime createdAt
) {}