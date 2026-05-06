package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.GroceryItem;

@Repository
public interface GroceryItemRepo extends JpaRepository<GroceryItem, Long> {
}

