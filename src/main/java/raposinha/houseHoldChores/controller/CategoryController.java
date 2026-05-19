package raposinha.houseHoldChores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.category.CategoryRequestDTO;
import raposinha.houseHoldChores.DTO.category.CategoryResponseDTO;
import raposinha.houseHoldChores.DTO.category.CategoryWithTasksResponseDTO;
import raposinha.houseHoldChores.service.CategoryService;

import java.util.List;

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

    @GetMapping("/name")
    public ResponseEntity<List<CategoryWithTasksResponseDTO>> getSingleCategory (@PathVariable("name") String name){
        return ResponseEntity.ok(categoryService.getSingleCategory(name));
    }
}
