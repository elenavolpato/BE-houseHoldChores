package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.SendInvitationDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.tools.EmailSender;

@Service
@AllArgsConstructor

public class InvitationService {

    private final UserRepo userRepo;
    private final EmailSender emailSender;

    @Transactional
    public void processAndSendInvitation(User inviter, SendInvitationDTO dto) {
        // Business Rule 1: Check if already registered
        if (userRepo.existsByEmail(dto.recipientEmail())) {
            throw new IllegalArgumentException("This email address is already registered.");
        }

        // Business Rule 2: Fire request to your Mailgun wrapper tool
        emailSender.sendInvitationEmail(inviter, dto.recipientEmail(), dto.recipientName());
    }
}
