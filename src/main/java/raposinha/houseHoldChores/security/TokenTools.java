package raposinha.houseHoldChores.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.exception.ForbiddenException;
import raposinha.houseHoldChores.exception.UnauthorizedException;

import java.util.Date;
import java.util.UUID;

@Component
public class TokenTools {

    protected final int duration = 1000 * 60 * 60 * 12; // 12 hours
    private final String secret;

    public TokenTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }


    public String createToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + duration))
                .subject(String.valueOf(user.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parse(token);
        } catch (ExpiredJwtException ex) {
            throw new UnauthorizedException("Your session has expired. Please log in again."); // → 401
        } catch (Exception ex) {
            throw new ForbiddenException("Invalid token."); // → 403
        }
    }

    public UUID extractFromToken(String token) {
        return UUID.fromString(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

}
