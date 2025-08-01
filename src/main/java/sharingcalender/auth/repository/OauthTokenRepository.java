package sharingcalender.auth.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import sharingcalender.auth.dto.oauth.OauthTokenSaveParam;

@Repository
@RequiredArgsConstructor
public class OauthTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;


    public void saveTokenInRedis(OauthTokenSaveParam oauthTokenSaveParam) {
        String hashKey = oauthTokenSaveParam.email();
        redisTemplate.opsForHash().put(hashKey, "access_token", oauthTokenSaveParam.access_token());
        redisTemplate.opsForHash().put(hashKey, "refresh_token", oauthTokenSaveParam.refresh_token());
        redisTemplate.opsForHash().put(hashKey, "token_type", oauthTokenSaveParam.token_type());
        redisTemplate.expire(hashKey, Duration.ofSeconds(oauthTokenSaveParam.expires_in())); // 3600 초

    }

    public void deleteNaveTokenInRedis(String username) {
        redisTemplate.delete(username);
    }
}
