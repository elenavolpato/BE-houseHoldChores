package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.GroceryItem;

import java.util.List;

@Repository
public interface GroceryItemRepo extends JpaRepository<GroceryItem, Long> {
    @Modifying
    @Query("DELETE FROM GroceryItem g WHERE g.id IN :ids AND g.groceryList.id = :listId")
    void deleteByIdInAndGroceryListId(@Param("ids") List<Long> ids, @Param("listId") Long listId);
}

