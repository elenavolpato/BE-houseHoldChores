package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.UserRegistrationDTO;
import raposinha.houseHoldChores.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // 200
    public UUID registerUser(@Valid @RequestBody UserRegistrationDTO body){
        return userService.save(body);
    }
}
