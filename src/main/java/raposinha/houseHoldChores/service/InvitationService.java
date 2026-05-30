package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.SendInvitationEmailDTO;
import raposinha.houseHoldChores.entities.Invitation;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.exception.BadRequestException;
import raposinha.houseHoldChores.exception.EmailAlreadyExistsException;
import raposinha.houseHoldChores.exception.InvalidTokenException;
import raposinha.houseHoldChores.repositories.InvitationRepo;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.tools.EmailSender;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvitationService {

    private final UserRepo userRepo;
    private final EmailSender emailSender;
    private final InvitationRepo invitationRepo;


    @Transactional
    public void processAndSendInvitation(User inviter, SendInvitationEmailDTO dto, String frontendBaseUrl) {
        // stop execution if account already exists
        if (userRepo.existsByEmail(dto.recipientEmail())) {
            throw new BadRequestException("This email address is already registered.");
        }

        // generate secure token
        String secureToken = UUID.randomUUID().toString();


        Invitation invitation = new Invitation();
        invitation.setToken(secureToken);
        invitation.setRecipientEmail(dto.recipientEmail());
        invitation.setGroupId(inviter.getGroup().getId()); // Link to household
        invitation.setInviterId(inviter.getId());         // Track sender
        invitation.setExpiryDate(LocalDateTime.now().plusDays(7));
        invitation.setUsed(false);

        invitationRepo.save(invitation);


        String secureInviteUrl = String.format("%s/register-with-invite?token=%s", frontendBaseUrl, secureToken);
        System.out.println("DEBUG SECURE LINK GENERATED: " + secureInviteUrl);

        emailSender.sendInvitationEmail(
                inviter,
                dto.recipientEmail(),
                dto.recipientName(),
                secureInviteUrl
        );
    }

    public Invitation getValidInvitationByToken(String token) {
        Invitation invitation = invitationRepo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("The invitation token does not exist."));

        if (invitation.isExpired()) {
            throw new InvalidTokenException("This invitation link has expired or has already been used.");
        }

        return invitation;
    }

    @Transactional
    public void expireToken(String token) {
        Invitation invitation = invitationRepo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found."));

        invitation.setUsed(true);
        invitationRepo.save(invitation);
    }
}
