package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
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
    @Transactional
    public UUID save(UserRegistrationDTO body){
        System.out.println("DEBUG: Save method started for email: " + body.getEmail());
        // check if email is already in use
        if(this.userRepo.existsByEmail(body.getEmail()))
            throw new BadRequestException("Email already in use");

        User newUser = new User(
            body.getUsername(),
            body.getEmail(),
            passwordEncoder.encode(body.getPassword())
        );

        // persistence
        User savedUser;
        try {
            savedUser = this.userRepo.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Database error: could not save user.");
        }

        // email Logic
        try {
            this.emailSender.sendRegistrationEmail(savedUser);
        } catch (Exception e) {
            System.err.println("CRITICAL: User created but email failed: " + e.getMessage());
        }

        return savedUser.getId();


    }
    // update user

    // add user to group

}
