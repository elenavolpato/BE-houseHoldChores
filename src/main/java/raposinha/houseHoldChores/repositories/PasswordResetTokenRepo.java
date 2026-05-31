package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import raposinha.houseHoldChores.entities.PasswordResetToken;
import raposinha.houseHoldChores.entities.User;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    @Modifying(clearAutomatically = true) // 💡 Clear automatically syncs persistence context
    void deleteByUser(User user);
}