package raposinha.houseHoldChores.DTO.groceries;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record BulkItemRequestWrapperDTO(
        @NotEmpty(message = "The items list cannot be empty")
        List<@Valid GroceryItemRequestDTO> items // 👈 Validates each element inside the list
) {}
