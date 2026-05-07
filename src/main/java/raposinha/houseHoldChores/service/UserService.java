package raposinha.houseHoldChores.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.UserRegistrationDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.exception.BadRequestException;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.tools.EmailSender;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    // USER REGISTRATION LOGIC
    public UUID save(UserRegistrationDTO body){
        // check if email is already in use
        if(this.userRepo.existsByEmail(body.getEmail()))
            throw new BadRequestException("Email already in use");

        User newUser = new User(
            body.getUsername(),
            body.getEmail(),
            passwordEncoder.encode(body.getPassword())
        );

        try {
            User savedUser = this.userRepo.save(newUser);
            return savedUser.getId();

        } catch (DataIntegrityViolationException e) {
            // catches database-level errors
            throw new BadRequestException("Internal database error while saving user.");

        }
    }
    // update user

    // add user to group

}
