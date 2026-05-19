package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.PresetTask;

import java.util.List;
@Repository
public interface PresetTaskRepo extends JpaRepository<PresetTask, Long> {
    List<PresetTask> findByCategory(String cat);

    @Query("SELECT DISTINCT p FROM PresetTask p WHERE p.category.id = :categoryId AND (p.group.id IS NULL OR p.group.id = :groupId)")
    List<PresetTask> findPresetsByCategoryAndGroup(
            @Param("categoryId") Long categoryId,
            @Param("groupId") Long groupId
    );

}
