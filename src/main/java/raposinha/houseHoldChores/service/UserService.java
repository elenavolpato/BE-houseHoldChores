package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.user.UserRegistrationRequestDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationResponseDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.exception.BadRequestException;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.repositories.GroupRepo;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.tools.EmailSender;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepo groupRepo;

    // USER REGISTRATION LOGIC
    @Transactional
    public UserRegistrationResponseDTO save(UserRegistrationRequestDTO body){
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

        //  send email Logic
        try {
            this.emailSender.sendRegistrationEmail(savedUser);
        } catch (Exception e) {
            System.err.println("CRITICAL: User created but email failed: " + e.getMessage());
        }
        return new UserRegistrationResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getAvatarUrl(), null);
    }

    // update avatar URL
    @Transactional
    public String changeAvatarUrl(UUID id, String avatarUrl) {
        User found = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " was not found"));

        found.setAvatarUrl(avatarUrl);
        return "Avatar changed successfully.";
    }

    public User findById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }
}
