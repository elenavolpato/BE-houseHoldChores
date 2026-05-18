package raposinha.houseHoldChores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raposinha.houseHoldChores.DTO.CategoryRequestDTO;
import raposinha.houseHoldChores.DTO.CategoryResponseDTO;
import raposinha.houseHoldChores.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/new")
    public CategoryResponseDTO addCategory(@RequestBody CategoryRequestDTO input) {
        return categoryService.saveAndReturnDTO(
                input.getTitle(),
                input.getDescription(),
                input.getIcon()
        );
    }
}
