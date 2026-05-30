package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.user.UpdateProfileRequestDTO;
import raposinha.houseHoldChores.DTO.user.UserProfileResponseDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationRequestDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationResponseDTO;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.entities.enums.GroupRole;
import raposinha.houseHoldChores.exception.BadRequestException;
import raposinha.houseHoldChores.exception.EmailAlreadyExistsException;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.repositories.GroupRepo;
import raposinha.houseHoldChores.repositories.InvitationRepo;
import raposinha.houseHoldChores.repositories.TaskRepo;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.tools.EmailSender;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final InvitationRepo invitationRepo;
    private final TaskRepo taskRepo;
    private final GroupRepo groupRepo;

    @Transactional
    public User save(UserRegistrationRequestDTO body){
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
        return savedUser;
    }

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

    @Transactional
    public UserProfileResponseDTO getUserProfile(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User account not found"));

        return new UserProfileResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getGroup() != null ? user.getGroup().getId() : null,
                user.getGroup() != null ? user.getGroup().getGroupName() : null,
                user.getRole()
        );
    }

    @Transactional
    public UserProfileResponseDTO updateBasicInfo(UUID userId, UpdateProfileRequestDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User account not found"));

        // Check if email is being changed and is already taken by someone else
        if (!user.getEmail().equalsIgnoreCase(dto.email()) && userRepo.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email address is already in use by another user.");
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());

        userRepo.saveAndFlush(user);

        return new UserProfileResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getGroup() != null ? user.getGroup().getId() : null,
                user.getGroup() != null ? user.getGroup().getGroupName() : null,
                user.getRole()
        );
    }

    @Transactional
    public void deleteUserAccount(UUID userId, UUID successorId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // cleans invitations and unassigns tasks
        taskRepo.unassignTasksByUserId(userId);
        invitationRepo.deleteByInviterId(userId);
        invitationRepo.deleteByRecipientEmail(user.getEmail());

        // Force database to clear task/invitation rows immediately
        userRepo.flush();

        //  Handle Group Ownership Transition if they own a group
        Group ownedGroup = groupRepo.findByOwnerId(userId).orElse(null);

        if (ownedGroup != null) {
            if (successorId != null) {
                User successor = userRepo.findById(successorId)
                        .orElseThrow(() -> new NotFoundException("Chosen successor user not found."));

                // Pass the torch on the group entity
                ownedGroup.setOwner(successor);
                groupRepo.saveAndFlush(ownedGroup);

                // Update role attributes on the successor
                successor.setRole(GroupRole.ADMIN);
                userRepo.saveAndFlush(successor);
            } else {
                // No successor provided means they are the last member. Delete the entire group layout.
                groupRepo.delete(ownedGroup);
                groupRepo.flush();
            }
        }
        userRepo.delete(user);
    }
}
