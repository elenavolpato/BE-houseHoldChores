package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.SendInvitationEmailDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.exception.EmailAlreadyExistsException;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.tools.EmailSender;

@Service
@AllArgsConstructor

public class InvitationService {

    private final UserRepo userRepo;
    private final EmailSender emailSender;

    @Transactional
    public void processAndSendInvitation(User inviter, SendInvitationEmailDTO dto) {
        // check if already registered
        if (userRepo.existsByEmail(dto.recipientEmail())) {
            throw new EmailAlreadyExistsException("This email address is already registered.");
        }

        emailSender.sendInvitationEmail(inviter, dto.recipientEmail(), dto.recipientName());
    }
}
