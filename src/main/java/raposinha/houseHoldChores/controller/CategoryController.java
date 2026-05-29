package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.category.CategoryRequestDTO;
import raposinha.houseHoldChores.DTO.category.CategoryResponseDTO;
import raposinha.houseHoldChores.DTO.category.CategoryWithTasksResponseDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;
    // POST /api/category/new
    @PostMapping("/new")
    public CategoryResponseDTO addCategory(@Valid @RequestBody CategoryRequestDTO input) {
        return categoryService.saveAndReturnDTO(
                input.getTitle(),
                input.getDescription(),
                input.getIcon(),
                input.getColorCode()
        );
    }
    // GET /api/category/filter/name?=Kitchen
    @GetMapping("/filter")
    public ResponseEntity<CategoryWithTasksResponseDTO> getCategoryWithTasks (
            @RequestParam("name") String name,
            @AuthenticationPrincipal User user){
        CategoryWithTasksResponseDTO res = categoryService.getCategoryWithTasks(name, user);
        return ResponseEntity.ok(res);
    }

    // GET /api/categories
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
