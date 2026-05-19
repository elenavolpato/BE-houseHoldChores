package raposinha.houseHoldChores.DTO.groceries;

import jakarta.validation.constraints.Min;

public record UpdateGroceryItemFieldsDTO(
        String name,
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,
        Boolean bought
) {}