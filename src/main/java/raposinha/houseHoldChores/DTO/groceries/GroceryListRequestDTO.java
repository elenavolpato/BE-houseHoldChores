package raposinha.houseHoldChores.DTO.groceries;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GroceryListRequestDTO(
        @NotBlank(message = "The grocery list name cannot be blank")
        @Size(max = 255, message = "The name must be less than 255 characters")
        String name,

        @NotNull(message = "The group ID is required")
        Long groupId
) {}
