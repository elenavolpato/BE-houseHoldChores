package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.GroceryList;
import java.util.List;

@Repository
public interface GroceryListRepo extends JpaRepository<GroceryList, Long> {
    // Derived helper method to grab all lists belonging to a specific family group later
    List<GroceryList> findByGroupId(Long groupId);
}
