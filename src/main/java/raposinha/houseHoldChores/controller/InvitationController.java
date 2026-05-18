package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.SendInvitationDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.InvitationService;
import raposinha.houseHoldChores.tools.EmailSender;

import java.util.Map;

@RestController
@RequestMapping("/api/invitations")
@AllArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendInvitation(
            @Valid @RequestBody SendInvitationDTO dto,
            @AuthenticationPrincipal User inviter) {

        invitationService.processAndSendInvitation(inviter, dto);
        return ResponseEntity.ok(Map.of(
                "message", "Invitation email successfully sent via Mailgun to " + dto.recipientEmail()
        ));
    }
}
