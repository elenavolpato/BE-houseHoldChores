package raposinha.houseHoldChores.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.CategoryResponseDTO;
import raposinha.houseHoldChores.entities.Category;
import raposinha.houseHoldChores.repositories.CategoryRepo;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public Category createCategory(String name, String desc, String icon) {
        Category c = new Category(name);
        c.setDescription(desc);
        // fallback icon, in case user does not select one
        if(icon == null){ c.setIcon("house");     }
        else {c.setIcon(icon);}
        return c;
    }

    public CategoryResponseDTO saveAndReturnDTO(String name, String desc, String icon) {
        // 1. Create the Entity using your logic
        Category category = new Category(name);
        category.setDescription(desc);
        category.setIcon(icon == null ? "house" : icon);

        // 2. Persist to Database
        Category saved = categoryRepo.save(category);

        // 3. Manually "Map" to the DTO
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(saved.getId());
        dto.setTitle(saved.getName()); // Mapping name -> title
        dto.setDescription(saved.getDescription());
        dto.setIcon(saved.getIcon());

        return dto;
    }


}
