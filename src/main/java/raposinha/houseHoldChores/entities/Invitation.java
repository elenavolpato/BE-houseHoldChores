package raposinha.houseHoldChores.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invitations")
@Getter
@Setter
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String recipientEmail;
    private Long groupId;
    private UUID inviterId;

    private boolean used = false; // Tracks if it has already been redeemed
    private LocalDateTime expiryDate;

    public boolean isExpired() {
        return this.used || LocalDateTime.now().isAfter(this.expiryDate);
    }
}