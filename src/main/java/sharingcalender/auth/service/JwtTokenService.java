package sharingcalender.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sharingcalender.auth.repository.RefreshTokenRepository;
import sharingcalender.auth.dto.TokenResponseDto;
import sharingcalender.auth.jwt.JwtUtil;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponseDto issueToken(String username, String role) {
        String accessToken = jwtUtil.createAccessJwt(username, role);

        String refreshToken = jwtUtil.createRefreshJwt(username, role);

        refreshTokenRepository.saveRefreshToken(username, role, refreshToken);

        return new TokenResponseDto(accessToken, refreshToken);
    }


    public boolean deleteRefreshToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        if (!refreshTokenRepository.isExistRefreshToken(username, role)) {
            return false;
        }
        refreshTokenRepository.deleteRefreshToken(username, role);

        return true;
    }
}
