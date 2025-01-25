package sharingcalender.auth.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_PREFIX = "refresh_token:";

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    public boolean isExistRefreshToken(String username, String role) {
        String key = REFRESH_PREFIX + username + ":" + role;

        return redisTemplate.hasKey(key);
    }

    public void saveRefreshToken(String username, String role, String refreshToken) {
        redisTemplate.opsForValue().set(REFRESH_PREFIX + username + ":" + role, refreshToken,
            Duration.ofMillis(refreshExpirationTime));
    }

    public String getRefreshToken(String username, String role) {
        String key = REFRESH_PREFIX + username + ":" + role;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String username, String role) {
        String key = REFRESH_PREFIX + username + ":" + role;
        redisTemplate.delete(key);
    }

}
