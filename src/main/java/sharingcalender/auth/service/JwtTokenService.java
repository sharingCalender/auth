package sharingcalender.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sharingcalender.auth.exception.BadRequestException;
import sharingcalender.auth.exception.UnAuthorizedException;
import sharingcalender.auth.repository.RefreshTokenRepository;
import sharingcalender.auth.dto.jwt.response.JwtTokenResponseDto;
import sharingcalender.auth.jwt.JwtUtil;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenResponseDto issueToken(String username, String role) {

        if (refreshTokenRepository.isExistRefreshToken(username, role)) {
            throw new UnAuthorizedException("이미 로그인한 계정입니다.");
        }

        String accessToken = jwtUtil.createAccessJwt(username, role);

        String refreshToken = jwtUtil.createRefreshJwt(username, role);

        refreshTokenRepository.saveRefreshToken(username, role, refreshToken);

        return new JwtTokenResponseDto(accessToken, refreshToken);
    }

    public JwtTokenResponseDto reissueToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String savedRefreshToken = refreshTokenRepository.getRefreshToken(username, role);

        if (!savedRefreshToken.equals(refreshToken)) {
            throw new BadRequestException("Refresh Token Is Not Valid");
        }

        refreshTokenRepository.deleteRefreshToken(username, role);

        return issueToken(username, role);
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
