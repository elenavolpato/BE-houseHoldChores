package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.group.GroupResponseDTO;
import raposinha.houseHoldChores.DTO.user.*;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.repositories.GroupRepo;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.service.GroupService;
import raposinha.houseHoldChores.service.UserService;

import java.util.List;
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
    @PatchMapping("/avatar")
    public ResponseEntity<String> changeAvatar(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateAvatarRequestDTO dto) { // 👈 Strong typed validation

        String message = userService.changeAvatarUrl(user.getId(), dto.avatarUrl());
        return ResponseEntity.ok(message);
    }

    // GET /api/users/me
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> getMyProfile(@AuthenticationPrincipal User user) {
        // Wrap the details into a clean DTO so you don't leak the password hash!
        UserProfileResponseDTO profile = userService.getUserProfile(user.getId());
        return ResponseEntity.ok(profile);
    }

    // PUT /api/users/profile
    @PutMapping("/profile")
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

    // DELETE /api/users/group/leave
    @DeleteMapping("/group/leave")
    public ResponseEntity<String> leaveGroup(@AuthenticationPrincipal User user) {
        //userService.leaveCurrentGroup(user.getId());
        return ResponseEntity.ok("You have successfully left the household group.");
    }




}
