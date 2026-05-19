package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.Category;
import raposinha.houseHoldChores.entities.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    // title of categories are unique - this checks if they are already in use
    boolean existsByName(String category);

    Optional<Category> findByName(String field);

    // fetch categories
    Optional<List<Category>> findById(Long id);

    

}
