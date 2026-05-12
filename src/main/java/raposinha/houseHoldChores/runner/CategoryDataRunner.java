package raposinha.houseHoldChores.runner;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raposinha.houseHoldChores.entities.Category;
import raposinha.houseHoldChores.repositories.CategoryRepo;

import java.util.List;

@Component
public class CategoryDataRunner implements CommandLineRunner {
    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public void run(String @NonNull ... args) throws Exception {
        List<Category> baseCategories = List.of(
            createCategory("Cleaning", "General house cleaning tasks", "broom-icon"),
            createCategory("Groceries", "Shopping and food supplies", "shopping-cart"),
            createCategory("Laundry", "Washing, drying, and folding", "tshirt"),
            createCategory("Garden", "Outdoor maintenance and plants", "leaf"),
            createCategory("Bills", "Monthly payments and finances", "money-bill")
        );

        for (Category cat : baseCategories) {
            if (!categoryRepo.existsByName(cat.getName())) {
                categoryRepo.save(cat);
                System.out.println("SEEDER: Category '" + cat.getName() + "' created.");
            }
        }
    }

    private Category createCategory(String name, String desc, String icon) {
        Category c = new Category(name);
        c.setDescription(desc);
        c.setIcon(icon);
        return c;
    }
}
