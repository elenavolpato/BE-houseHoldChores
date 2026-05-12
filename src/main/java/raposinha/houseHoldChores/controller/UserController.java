package raposinha.houseHoldChores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

   @Autowired
   private UserService userService;

    // update avatar
    @PatchMapping("/avatar")
    public ResponseEntity<String> changeAvatar(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> body
    ) {
       String avatarUrl = body.get("avatarUrl");
       String message = userService.changeAvatarUrl(user.getId(), avatarUrl);
       return ResponseEntity.ok(message);
    }


}
