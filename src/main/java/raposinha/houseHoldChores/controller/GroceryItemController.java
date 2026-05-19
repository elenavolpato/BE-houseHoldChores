package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.groceries.BulkItemRequestWrapperDTO;
import raposinha.houseHoldChores.DTO.groceries.DeleteGroceryItemsRequestDTO;
import raposinha.houseHoldChores.DTO.groceries.GroceryItemResponseDTO;
import raposinha.houseHoldChores.DTO.groceries.UpdateGroceryItemFieldsDTO;
import raposinha.houseHoldChores.service.GroceryItemService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grocery-items")
@AllArgsConstructor
public class GroceryItemController {

    private final GroceryItemService groceryItemService;

    @PostMapping("/{groceryListId}/new")
    public ResponseEntity<List<GroceryItemResponseDTO>> addItems(
            @PathVariable("groceryListId") Long groceryListId,
            @Valid @RequestBody BulkItemRequestWrapperDTO wrapper) {

        List<GroceryItemResponseDTO> response = groceryItemService.addGroceryItems(groceryListId, wrapper.items());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groceryListId}/delete")
    public ResponseEntity<Map<String, String>> removeItems(
            @PathVariable("groceryListId") Long groceryListId,
            @Valid @RequestBody DeleteGroceryItemsRequestDTO dto) {

        groceryItemService.removeGroceryItems(groceryListId, dto);
        return ResponseEntity.ok(Map.of(
                "message", "Successfully deleted requested items from grocery list " + groceryListId
        ));
    }

    @PatchMapping("/{groceryListId}/update/{itemId}")
    public ResponseEntity<GroceryItemResponseDTO> updateItemFields(
            @PathVariable("groceryListId") Long groceryListId,
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody UpdateGroceryItemFieldsDTO dto) {

        GroceryItemResponseDTO response = groceryItemService.updateItemFields(groceryListId, itemId, dto);
        return ResponseEntity.ok(response);
    }

}
