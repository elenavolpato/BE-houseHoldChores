package raposinha.houseHoldChores.DTO.groceries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryItemResponseDTO {
    private Long id;
    private String item;
    private int quantity;
    private boolean isBought;
}
