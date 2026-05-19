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
import java.util.Map;

@Component
@Order(1)
public class CategoryDataRunner implements CommandLineRunner {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    public CategoryService categoryService;

    @Override
    public void run(String @NonNull ... args) throws Exception {

        Map<String, String[]> categoriesToSeed = Map.of(
                "Cleaning", new String[]{"General house cleaning tasks", "broom-icon"},
                "Groceries", new String[]{"Shopping and food supplies", "shopping-cart"},
                "Laundry", new String[]{"Washing, drying, and folding", "tshirt"},
                "Garden", new String[]{"Outdoor maintenance and plants", "leaf"},
                "Bills", new String[]{"Monthly payments and finances", "money-bill"},
                "Kitchen", new String[]{"Cooking, washing dishes, and cleaning", "pan"},
                "Pets", new String[]{"Pets care ans cleaning", "dog"}
        );
        categoriesToSeed.forEach((name, details) -> {
            // check if entity is already created
            if (!categoryRepo.existsByName(name)) {
                String description = details[0];
                String icon = details[1];

                Category cat = categoryService.createCategory(name, description, icon);

                // 2. FORCE Hibernate to write it to disk immediately
                categoryRepo.saveAndFlush(cat);

                System.out.println("🌱 SEEDER: Category '" + name + "' created and flushed.");
            } else {
                System.out.println("✅ SEEDER: Category '" + name + "' already exists. Skipping.");
            }
        });
    }


}
