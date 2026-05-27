package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.user.LoginRequestDTO;
import raposinha.houseHoldChores.DTO.user.LoginResponseDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationRequestDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationResponseDTO;
import raposinha.houseHoldChores.service.AuthService;
import raposinha.houseHoldChores.service.UserService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    // POST /api/auth/register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDTO register(@Valid @RequestBody UserRegistrationRequestDTO body) {
        return userService.save(body);
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO body) {
        return authService.authenticate(body);
    }
}
