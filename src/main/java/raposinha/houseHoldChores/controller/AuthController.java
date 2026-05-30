package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.RegisterWithInviteDTO;
import raposinha.houseHoldChores.DTO.user.LoginRequestDTO;
import raposinha.houseHoldChores.DTO.user.LoginResponseDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationRequestDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationResponseDTO;
import raposinha.houseHoldChores.entities.Invitation;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.AuthService;
import raposinha.houseHoldChores.service.GroupService;
import raposinha.houseHoldChores.service.InvitationService;
import raposinha.houseHoldChores.service.UserService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final GroupService groupService;
    private final InvitationService invitationService;

    // POST /api/auth/register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDTO register(@Valid @RequestBody UserRegistrationRequestDTO body) {

        User savedUser = userService.save(body);

        return new UserRegistrationResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getAvatarUrl(),
                null
        );
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO body) {
        return authService.authenticate(body);
    }

    // POST /api/auth/register-with-invite
    @PostMapping("/register-with-invite")
    public ResponseEntity<UserRegistrationResponseDTO> registerWithInvite(@Valid @RequestBody RegisterWithInviteDTO dto) {

        Invitation invitation = invitationService.getValidInvitationByToken(dto.token());

        UserRegistrationRequestDTO registrationRequest = new UserRegistrationRequestDTO(
                dto.username(),
                dto.email(),
                dto.password()
        );

        // 3. 🚀 REUSE EXISTING LOGIC: Hand off to your UserService to create the user and send the email
        User newlyRegisteredUser = userService.save(registrationRequest);
        System.out.println("hahahaha --->" +newlyRegisteredUser);
        // 4. Automatically bond this brand-new user to the household group
        groupService.addUserToGroup( invitation.getGroupId(),newlyRegisteredUser.getId(), invitation.getInviterId());

        // 5. Invalidate the invitation link so it can't be recycled
        invitationService.expireToken(dto.token());

        // 6. Return your standard client response DTO cleanly
        UserRegistrationResponseDTO response = new UserRegistrationResponseDTO(
                newlyRegisteredUser.getId(),
                newlyRegisteredUser.getUsername(),
                newlyRegisteredUser.getEmail(),
                newlyRegisteredUser.getAvatarUrl(),
                null
        );

        return ResponseEntity.ok(response);
    }

}
