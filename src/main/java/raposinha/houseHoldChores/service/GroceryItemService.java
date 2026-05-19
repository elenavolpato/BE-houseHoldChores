package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.groceries.DeleteGroceryItemsRequestDTO;
import raposinha.houseHoldChores.DTO.groceries.GroceryItemRequestDTO;
import raposinha.houseHoldChores.DTO.groceries.GroceryItemResponseDTO;
import raposinha.houseHoldChores.DTO.groceries.UpdateGroceryItemFieldsDTO;
import raposinha.houseHoldChores.entities.GroceryItem;
import raposinha.houseHoldChores.entities.GroceryList;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.repositories.GroceryItemRepo;
import raposinha.houseHoldChores.repositories.GroceryListRepo;
import raposinha.houseHoldChores.repositories.GroupRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GroceryItemService {

    private final GroceryItemRepo groceryItemRepo;
    private final GroupRepo groupRepo;
    private final GroceryListRepo groceryListRepo;

    @Transactional
    public List<GroceryItemResponseDTO> addGroceryItems(Long groceryListId, List<GroceryItemRequestDTO> dtos ){
         // verify that the list exists
        GroceryList groceryList = groceryListRepo.findById(groceryListId)
                .orElseThrow(() -> new NotFoundException("Grocery list not found with ID: " + groceryListId));

        List<GroceryItemResponseDTO> responseList = new ArrayList<>();
        for (GroceryItemRequestDTO dto: dtos) {
            GroceryItem item = new GroceryItem();
            item.setName(dto.getName());
            item.setQuantity(dto.getQuantity() != 1 ? dto.getQuantity(): 1);
            item.setBought(false);

            // connect the items to the list
            item.setGroceryList(groceryList);

            // persist item to your grocery_items table
            GroceryItem savedItem = groceryItemRepo.save(item);

            responseList.add(new GroceryItemResponseDTO(
                    savedItem.getId(),
                    savedItem.getName(),
                    savedItem.getQuantity(),
                    savedItem.isBought()
            ));
        }
        return responseList;
    }

    @Transactional
    public void removeGroceryItems(Long groceryListId, DeleteGroceryItemsRequestDTO dto){
        if (!groceryListRepo.existsById(groceryListId)) {
            throw new NotFoundException("Grocery list not found with ID: " + groceryListId);
        }
        groceryItemRepo.deleteByIdInAndGroceryListId(dto.itemIds(), groceryListId);
    }

    @Transactional
    public GroceryItemResponseDTO updateItemFields(Long groceryListId, Long itemId, UpdateGroceryItemFieldsDTO dto) {
        if (!groceryListRepo.existsById(groceryListId)) {
            throw new NotFoundException("Grocery list not found with ID: " + groceryListId);
        }

        GroceryItem item = groceryItemRepo.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with ID: " + itemId));

        if (!item.getGroceryList().getId().equals(groceryListId)) {
            throw new IllegalArgumentException("The requested item does not belong to this grocery list.");
        }

        if (dto.quantity() != null) {
            item.setQuantity(dto.quantity());
        }

        if (dto.bought() != null) {
            item.setBought(dto.bought());
        }

        if(dto.name() != null){
            item.setName(dto.name());
        }

        return new GroceryItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.isBought()
        );
    }
}
