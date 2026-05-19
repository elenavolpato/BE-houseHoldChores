package raposinha.houseHoldChores.DTO.groceries;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroceryItemRequestDTO {

    @NotBlank(message = "Item name is required")
    private String name;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

//    @NotNull(message = "Items must be linked to a group")
//    private Long groupId;
}
