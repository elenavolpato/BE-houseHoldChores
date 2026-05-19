package raposinha.houseHoldChores.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.category.CategoryRequestDTO;
import raposinha.houseHoldChores.DTO.category.CategoryResponseDTO;
import raposinha.houseHoldChores.DTO.category.CategoryWithTasksResponseDTO;
import raposinha.houseHoldChores.entities.User;
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

    @GetMapping("/filter")
    public ResponseEntity<CategoryWithTasksResponseDTO> getCategoryWithTasks (
            @RequestParam("name") String name,
            @AuthenticationPrincipal User user){
        CategoryWithTasksResponseDTO res = categoryService.getCategoryWithTasks(name, user);
        return ResponseEntity.ok(res);
    }
}
