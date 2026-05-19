package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.groceries.GroceryListRequestDTO;
import raposinha.houseHoldChores.DTO.groceries.GroceryListResponseDTO;
import raposinha.houseHoldChores.DTO.groceries.DuplicateListRequestDTO;
import raposinha.houseHoldChores.DTO.groceries.GroceryItemResponseDTO;
import raposinha.houseHoldChores.entities.GroceryItem;
import raposinha.houseHoldChores.entities.GroceryList;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.repositories.GroceryListRepo;
import raposinha.houseHoldChores.repositories.GroupRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroceryListService {

    private final GroceryListRepo groceryListRepo;
    private final GroupRepo groupRepo;

    @Transactional
    public GroceryListResponseDTO createGroceryList(GroceryListRequestDTO dto){
        // verify that the group exists
        Group group = groupRepo.findById(dto.groupId()).orElseThrow(() -> new NotFoundException("Household group now found. Id: " + dto.groupId()));

        GroceryList newList = new GroceryList();
        newList.setName(dto.name());
        newList.setGroup(group);
        newList.setCreatedAt(LocalDateTime.now());

        GroceryList savedList = groceryListRepo.save(newList);

        return new GroceryListResponseDTO(
                savedList.getId(),
                savedList.getName(),
                savedList.getGroup().getId(),
                savedList.getCreatedAt()
        );
    }

    public List<GroceryList> getAllListsOfGroup(Group group){
        return groceryListRepo.findByGroupId(group.getId());
    }

    @Transactional
    public List<GroceryItemResponseDTO> getSingleList(Long groupId, Long groceryListId) {
        GroceryList groceryList = groceryListRepo.findById(groceryListId)
                .orElseThrow(() -> new NotFoundException("Grocery list not found with ID: " + groceryListId));

        // does this list belong to the requested group?
        if (!groceryList.getGroup().getId().equals(groupId)) {
            throw new IllegalArgumentException("The requested grocery list does not belong to this household group.");
        }

        // map items to show them
        return groceryList.getItems().stream()
                .map(item -> new GroceryItemResponseDTO(
                        item.getId(),
                        item.getName(),
                        item.getQuantity(),
                        item.isBought()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public GroceryListResponseDTO duplicateGroceryList(Long groupId, Long groceryListId, DuplicateListRequestDTO dto) {
        GroceryList originalList = groceryListRepo.findById(groceryListId)
                .orElseThrow(() -> new NotFoundException("Original grocery list not found with ID: " + groceryListId));

        if (!originalList.getGroup().getId().equals(groupId)) {
            throw new IllegalArgumentException("The requested grocery list does not belong to this household group.");
        }

        GroceryList clonedList = new GroceryList();
        clonedList.setName(dto.newListName());
        // Keeps it in the same family group
        clonedList.setGroup(originalList.getGroup());
        clonedList.setCreatedAt(LocalDateTime.now());

        // clone all items from the original list
        for (GroceryItem originalItem : originalList.getItems()) {
            GroceryItem newItem = new GroceryItem();
            newItem.setName(originalItem.getName());
            newItem.setQuantity(originalItem.getQuantity());
            // all items in this new list should be saved as not bought yet
            newItem.setBought(false);

            newItem.setGroceryList(clonedList);

            clonedList.getItems().add(newItem);
        }

        GroceryList savedList = groceryListRepo.save(clonedList);

        return new GroceryListResponseDTO(
                savedList.getId(),
                savedList.getName(),
                savedList.getGroup().getId(),
                savedList.getCreatedAt()
        );
    }
}
