package raposinha.houseHoldChores.DTO.groceries;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record DeleteGroceryItemsRequestDTO(
        @NotEmpty(message = "You must provide at least one item ID to delete")
        List<Long> itemIds
) {}