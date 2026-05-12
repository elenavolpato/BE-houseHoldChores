package raposinha.houseHoldChores.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.LoginRequestDTO;
import raposinha.houseHoldChores.DTO.LoginResponseDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.exception.UnauthorizedException;
import raposinha.houseHoldChores.repositories.UserRepo;
import raposinha.houseHoldChores.security.TokenTools;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenTools tokenTools;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder, TokenTools tokenTools) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenTools = tokenTools;
    }



    public LoginResponseDTO authenticate(LoginRequestDTO body) {
        User user = userRepo.findByEmail(body.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));


        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = tokenTools.createToken(user);

        return new LoginResponseDTO(token, user.getUsername(), user.getAvatarUrl());
    }


}
