package sharingcalender.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTIme;

    public JwtUtil(
        @Value("${spring.jwt.secret}") String secret,
        @Value("${spring.jwt.token.access-expiration-time}") Long accessExpirationTime,
        @Value("${spring.jwt.token.refresh-expiration-time}") Long refreshExpirationTIme) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            SIG.HS256.key().build().getAlgorithm());
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTIme = refreshExpirationTIme;
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("category", String.class);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .getExpiration().before(new Date());
    }

    public String createAccessJwt(String username, String role) {
        return Jwts.builder()
            .claim("category", "access")
            .claim("username", username)
            .claim("role",role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + accessExpirationTime))
            .signWith(secretKey)
            .compact();

    }

    public String createRefreshJwt(String username, String role) {
        return Jwts.builder()
            .claim("category", "refresh")
            .claim("username", username)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + refreshExpirationTIme))
            .signWith(secretKey)
            .compact();
    }






}
