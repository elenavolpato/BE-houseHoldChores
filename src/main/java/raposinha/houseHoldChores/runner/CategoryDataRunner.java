package raposinha.houseHoldChores.runner;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import raposinha.houseHoldChores.entities.Category;
import raposinha.houseHoldChores.repositories.CategoryRepo;
import raposinha.houseHoldChores.service.CategoryService;

import java.util.List;

@Component
@Order(1)
public class CategoryDataRunner implements CommandLineRunner {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    public CategoryService categoryService;

    @Override
    public void run(String @NonNull ... args) throws Exception {
        List<Category> baseCategories = List.of(
                categoryService.createCategory("Cleaning", "General house cleaning tasks", "broom-icon"),
                categoryService.createCategory("Groceries", "Shopping and food supplies", "shopping-cart"),
                categoryService.createCategory("Laundry", "Washing, drying, and folding", "tshirt"),
                categoryService.createCategory("Garden", "Outdoor maintenance and plants", "leaf"),
                categoryService.createCategory("Bills", "Monthly payments and finances", "money-bill"),
                categoryService.createCategory("Kitchen", "Cooking, washing dishes, and cleaning", "tshirt"),
                categoryService.createCategory("Pets", "Pets care ans cleaning", "dog")
        );

        for (Category cat : baseCategories) {
            if (!categoryRepo.existsByName(cat.getName())) {
                categoryRepo.save(cat);
                System.out.println("SEEDER: Category '" + cat.getName() + "' created.");
            }
        }
    }


}
