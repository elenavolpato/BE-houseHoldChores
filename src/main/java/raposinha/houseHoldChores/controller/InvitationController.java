package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.SendInvitationEmailDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.InvitationService;

import java.util.Map;

@RestController
@RequestMapping("/api/invitations")
@AllArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    // POST /api/invitations/send
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendInvitation(
            @Valid @RequestBody SendInvitationEmailDTO dto,
            @AuthenticationPrincipal User inviter) {

        invitationService.processAndSendInvitation(inviter, dto);
        return ResponseEntity.ok(Map.of(
                "message", "Invitation email successfully sent via Mailgun to " + dto.recipientEmail()
        ));
    }
}
