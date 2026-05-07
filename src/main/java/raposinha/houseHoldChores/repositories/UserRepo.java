package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    // check if email is already used
    boolean existsByEmail(String email);

    // for authentication/login
    Optional<User> findByEmail(String email);

    // find all members of a specific house/group
    List<User> findByGroup_Id(String id);
}