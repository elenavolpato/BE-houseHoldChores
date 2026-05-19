package raposinha.houseHoldChores.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.category.CategoryResponseDTO;
import raposinha.houseHoldChores.DTO.category.CategoryWithTasksResponseDTO;
import raposinha.houseHoldChores.entities.Category;
import raposinha.houseHoldChores.exception.NotFoundException;
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
        Category category = new Category(name);
        category.setDescription(desc);
        category.setIcon(icon == null ? "house" : icon);

        Category saved = categoryRepo.save(category);

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(saved.getId());
        dto.setTitle(saved.getName()); // Mapping name -> title
        dto.setDescription(saved.getDescription());
        dto.setIcon(saved.getIcon());

        return dto;
    }

    public CategoryWithTasksResponseDTO getSingleCategory(String name){
        Category found = categoryRepo.findByName(name).orElseThrow(() -> new NotFoundException("No categories with this name were found."));
        return categoryRepo.findById(found.getId());
    }


}
