package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.SendInvitationEmailDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.InvitationService;

import java.util.Map;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;
    private final String frontendBaseUrl;

    public InvitationController(
            InvitationService invitationService,
            @Value("${app.frontend.url}") String frontendBaseUrl) {
        this.invitationService = invitationService;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendInvitation(
            @Valid @RequestBody SendInvitationEmailDTO dto,
            @AuthenticationPrincipal User inviter) {

        // 🚀 Clean hand-off to service layer with the injected base configuration URL
        invitationService.processAndSendInvitation(inviter, dto, this.frontendBaseUrl);

        return ResponseEntity.ok(Map.of(
                "message", "Invitation email successfully sent via Mailgun to " + dto.recipientEmail()
        ));
    }
}

