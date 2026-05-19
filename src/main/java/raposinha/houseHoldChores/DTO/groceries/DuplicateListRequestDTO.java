package raposinha.houseHoldChores.DTO.groceries;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DuplicateListRequestDTO(
        @NotBlank(message = "The new list name cannot be blank")
        @Size(max = 255, message = "The name must be less than 255 characters")
        String newListName
) {}
