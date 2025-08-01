package sharingcalender.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sharingcalender.auth.dto.jwt.response.JwtTokenResponseDto;
import sharingcalender.auth.dto.oauth.request.GetOauthUriRequestDto;
import sharingcalender.auth.dto.oauth.request.OauthTokenRequestDto;
import sharingcalender.auth.jwt.JwtUtil;
import sharingcalender.auth.repository.OauthTokenRepository;
import sharingcalender.auth.resolver.OauthLoginResolver;

@Service
@RequiredArgsConstructor
public class OauthService {


    private final JwtUtil jwtUtil;
    private final OauthTokenRepository oauthTokenRepository;
    private final OauthLoginResolver oauthLoginResolver;

    public JwtTokenResponseDto getJwtToken(OauthTokenRequestDto oauthTokenRequestDto) {
        return oauthLoginResolver.getJwtToken(oauthTokenRequestDto);
    }

    public String getOAuthUri(GetOauthUriRequestDto oauthUriRequestDto){
        return oauthLoginResolver.getOauthUri(oauthUriRequestDto);
    }


    public void deleteNaverTokenInRedis(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);

        oauthTokenRepository.deleteNaveTokenInRedis(username);
    }

}
