package raposinha.houseHoldChores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raposinha.houseHoldChores.entities.Invitation;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepo extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByToken(String token);
    void deleteByInviterId(UUID id);
    void deleteByRecipientEmail(String email);
}
