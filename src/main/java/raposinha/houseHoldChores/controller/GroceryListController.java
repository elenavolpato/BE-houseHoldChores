package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.groceries.GroceryListRequestDTO;
import raposinha.houseHoldChores.DTO.groceries.GroceryListResponseDTO;
import raposinha.houseHoldChores.DTO.groceries.DuplicateListRequestDTO;
import raposinha.houseHoldChores.DTO.groceries.GroceryItemResponseDTO;
import raposinha.houseHoldChores.entities.GroceryList;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.service.GroceryListService;

import java.util.List;

@RestController
@RequestMapping("/api/grocery-lists")
@AllArgsConstructor
public class GroceryListController {

    private final GroceryListService groceryListService;

    @PostMapping("/new")
    public ResponseEntity<GroceryListResponseDTO> createList(
            @Valid @RequestBody GroceryListRequestDTO dto) {

        GroceryListResponseDTO response = groceryListService.createGroceryList(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{group}")
    public List<GroceryList> getAllLists(@Valid @PathVariable Group group) {
        return groceryListService.getAllListsOfGroup(group);
    }
    @GetMapping("/group/{groupId}/list/{groceryListId}")
    public ResponseEntity<List<GroceryItemResponseDTO>> getSingleList(@Valid
            @PathVariable("groupId") Long groupId,
            @PathVariable("groceryListId") Long groceryListId)  {
        List<GroceryItemResponseDTO> items = groceryListService.getSingleList(groupId, groceryListId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/group/{groupId}/list/{groceryListId}/duplicate")
    public ResponseEntity<GroceryListResponseDTO> duplicateList(
            @PathVariable("groupId") Long groupId,
            @PathVariable("groceryListId") Long groceryListId,
            @Valid @RequestBody DuplicateListRequestDTO dto) {

        GroceryListResponseDTO response = groceryListService.duplicateGroceryList(groupId, groceryListId, dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}