package raposinha.houseHoldChores.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import raposinha.houseHoldChores.DTO.password.ForgotPasswordRequest;
import raposinha.houseHoldChores.DTO.password.ResetPasswordRequest;
import raposinha.houseHoldChores.entities.PasswordResetToken;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.repositories.PasswordResetTokenRepo;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.tools.EmailSender;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepo userRepository;
    private final PasswordResetTokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    private static final long EXPIRY_MINUTES = 30;

    public void processForgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            tokenRepository.deleteByUser(user);

            String token = UUID.randomUUID().toString();

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES));
            tokenRepository.save(resetToken);

            String resetLink = "https://raposinha.dev/reset-password?token=" + token;

            // ← matches your existing pattern of passing user details
            emailSender.sendPasswordResetEmail(
                    user.getEmail(),
                    user.getUsername(),
                    resetLink
            );
        });
    }

    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token"));

        if (resetToken.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token already used");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
