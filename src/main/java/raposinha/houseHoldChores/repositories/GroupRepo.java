package raposinha.houseHoldChores.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepo extends JpaRepository<Group, Long> {
    // find the group owned by a specific admin
    Optional<Group> findByOwnerId(UUID ownerId);
}