package raposinha.houseHoldChores.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raposinha.houseHoldChores.DTO.user.*;
import raposinha.houseHoldChores.entities.User;

import raposinha.houseHoldChores.service.GroupService;
import raposinha.houseHoldChores.service.UserService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Validated
public class UserController {

   private final UserService userService;
   private final GroupService groupService;

    // update avatar
    // PATCH /api/users/avatar
    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {

        String updatedAvatarUrl = userService.changeAvatarUrl(id, file);
        return ResponseEntity.ok(Map.of("avatarUrl", updatedAvatarUrl));
    }

    // GET /api/users/me
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> getMyProfile(@AuthenticationPrincipal User user) {
        UserProfileResponseDTO profile = userService.getUserProfile(user.getId());
        return ResponseEntity.ok(profile);
    }
    // PATCH /api/users/me/username
    @PatchMapping("/me/username")
    public ResponseEntity<UserProfileResponseDTO> updateUsername(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UpdateUsernameRequest request) {

        UserProfileResponseDTO updatedProfile = userService.updateMyUsername(currentUser.getId(), request.username());
        return ResponseEntity.ok(updatedProfile);
    }

    // PATCH /api/users/me/email
    @PatchMapping("/me/email")
    public ResponseEntity<UserProfileResponseDTO> updateEmail(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UpdateEmailRequest request) {

        UserProfileResponseDTO updatedProfile = userService.updateMyEmail(currentUser.getId(), request.email());
        return ResponseEntity.ok(updatedProfile);
    }

    // PUT /api/users/update-profile
    @PutMapping("/update-profile")
    public ResponseEntity<UserProfileResponseDTO> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequestDTO dto) {

        UserProfileResponseDTO updated = userService.updateBasicInfo(user.getId(), dto);
        return ResponseEntity.ok(updated);
    }

    // POST /api/users/group/join
    @PostMapping("/group/join")
    public ResponseEntity<String> joinHouseholdGroup(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody JoinGroupRequestDTO dto) { // dto contains String inviteCode

        //userService.joinGroup(user.getId(), dto.getInviteCode());
        return ResponseEntity.ok("Successfully joined the household group!");
    }

    // DELETE /api/users/delete
    @DeleteMapping("/delete")
    @Transactional // 🚀 Ensures database changes commit cleanly
    public ResponseEntity<Map<String, String>> deleteAccount(@AuthenticationPrincipal User user, UUID successorId) {

        userService.deleteUserAccount(user.getId(), successorId);

        return ResponseEntity.ok(Map.of(
                "message", "Your account has been successfully and permanently deleted."
        ));
    }



}
