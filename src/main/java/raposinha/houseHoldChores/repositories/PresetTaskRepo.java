package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.PresetTask;

import java.util.List;
@Repository
public interface PresetTaskRepo extends JpaRepository<PresetTask, Long> {
    List<PresetTask> findByCategory(String cat);

}
