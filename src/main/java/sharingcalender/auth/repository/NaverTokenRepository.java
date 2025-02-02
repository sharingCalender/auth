package sharingcalender.auth.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import sharingcalender.auth.dto.oauth.naver.response.NaverTokenIssueResponseDto;
import sharingcalender.auth.dto.oauth.naver.response.NaverUserInfoResponseDto;

@Repository
@RequiredArgsConstructor
public class NaverTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PROVIDER = "NAVER";

    public void saveTokenInRedis(NaverTokenIssueResponseDto tokenResponse,
        NaverUserInfoResponseDto userInfoResponse) {
        String hashKey = userInfoResponse.id() + "-" + PROVIDER;
        redisTemplate.opsForHash().put(hashKey, "access_token", tokenResponse.access_token());
        redisTemplate.opsForHash().put(hashKey, "refresh_token", tokenResponse.access_token());
        redisTemplate.opsForHash().put(hashKey, "token_type", tokenResponse.token_type());
        redisTemplate.expire(hashKey, Duration.ofSeconds(tokenResponse.expires_in())); // 3600 초

    }
}
