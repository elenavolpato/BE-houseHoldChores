package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class GroceryListController {

    private final GroceryListService groceryListService;

    // POST /api/grocery-lists/new
    @PostMapping("/new")
    public ResponseEntity<GroceryListResponseDTO> createList(
            @Valid @RequestBody GroceryListRequestDTO dto) {

        GroceryListResponseDTO response = groceryListService.createGroceryList(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //GET /api/grocery-lists/group/1
    @GetMapping("/group/{group}")
    public List<GroceryList> getAllLists(@PathVariable Group group) {
        return groceryListService.getAllListsOfGroup(group);
    }

    // GET /api/grocery-lists/group/1/list/2
    @GetMapping("/group/{groupId}/list/{groceryListId}")
    public ResponseEntity<List<GroceryItemResponseDTO>> getSingleList(
            @PathVariable("groupId") @Size(min=1) Long groupId,
            @PathVariable("groceryListId") @Size(min=1) Long groceryListId)  {
        List<GroceryItemResponseDTO> items = groceryListService.getSingleList(groupId, groceryListId);
        return ResponseEntity.ok(items);
    }

    // POST /api/grocery-lists/group/1/list/2/duplicate
    @PostMapping("/group/{groupId}/list/{groceryListId}/duplicate")
    public ResponseEntity<GroceryListResponseDTO> duplicateList(
            @PathVariable("groupId") Long groupId,
            @PathVariable("groceryListId") Long groceryListId,
            @Valid @RequestBody DuplicateListRequestDTO dto) {

        GroceryListResponseDTO response = groceryListService.duplicateGroceryList(groupId, groceryListId, dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}