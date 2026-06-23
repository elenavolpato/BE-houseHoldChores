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

    @Query("""
    SELECT DISTINCT p FROM PresetTask p
    WHERE p.category.id = :categoryId
    AND (p.group IS NULL OR p.group.id = :groupId)
""")
    List<PresetTask> findPresetsByCategoryAndGroup(
            @Param("categoryId") Long categoryId,
            @Param("groupId") Long groupId
    );

    // Fetches global rows (null group) + custom household rows side-by-side
    @Query("SELECT p FROM PresetTask p WHERE p.group IS NULL OR p.group.id = :groupId")
    List<PresetTask> findGlobalAndByGroupId(@Param("groupId") Long groupId);

    @Query("""
    SELECT p FROM PresetTask p
    WHERE (p.group IS NULL OR p.group.id = :groupId)
    AND NOT EXISTS (
        SELECT t FROM Task t
        WHERE t.sourcePreset = p AND t.group.id = :groupId
    )
""")
    List<PresetTask> findAvailablePresetsForGroup(@Param("groupId") Long groupId);

}
