package sharingcalender.auth.service;


import java.net.URLEncoder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import sharingcalender.auth.adapter.UserAdapter;
import sharingcalender.auth.adapter.oauth.NaverOAuthAdapter;
import sharingcalender.auth.adapter.oauth.NaverUserInfoAdapter;
import sharingcalender.auth.dto.TokenResponseDto;
import sharingcalender.auth.dto.oauth.naver.request.OAuthUserIsExistRequestDto;
import sharingcalender.auth.dto.oauth.naver.response.NaverInfoResponseDto;
import sharingcalender.auth.dto.oauth.naver.response.NaverTokenIssueResponseDto;
import sharingcalender.auth.dto.oauth.naver.response.NaverUserInfoResponseDto;

@Service
@RequiredArgsConstructor
public class NaverOAuthService {

    @Value("${oauth.naver.authorization-uri}")
    private String authorizationURI;


    @Value("${oauth.naver.redirect-uri}")
    private String redirectURI;

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.client-secret}")
    private String clientSecret;

    @Value("${oauth.naver.grant-type}")
    private String grant_type;

    private String responseType = "code";

    private static final String PROVIDER = "NAVER";

    private final NaverOAuthAdapter naverOAuthAdapter;
    private final NaverUserInfoAdapter naverUserInfoAdapter;

    private final UserAdapter userAdapter;

    private final JwtTokenService jwtTokenService;

    public String getOAuth2CodeUrl() {
        String state = URLEncoder.encode(UUID.randomUUID().toString());

        return UriComponentsBuilder.fromUriString(authorizationURI)
            .queryParam("response_type", responseType)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectURI)
            .queryParam("state", state)
            .build().toString();
    }

    public TokenResponseDto getJwtToken(String code, String state) {

        // access token 발급하기
        NaverTokenIssueResponseDto tokenResponse = getNaverAccessToken(code, state);

        // user info 가져오기
        NaverUserInfoResponseDto userInfoResponse = getNaverUserInfo(tokenResponse);

        // 기존회원인지 아닌지 여부 확인하고 가입처리
        oauthUserIsExist(userInfoResponse);

        //jwt 토큰 발급하고 리턴해서 프런트로 넘기기

        TokenResponseDto tokenResponseDto = jwtTokenService.issueToken(
            userInfoResponse.id() + "-" + PROVIDER, "USER");

        return tokenResponseDto;

    }

    private void oauthUserIsExist(NaverUserInfoResponseDto userInfoResponse) {
        userAdapter.oauthUserIsExist(
            new OAuthUserIsExistRequestDto(userInfoResponse.id(), userInfoResponse.name(),
                userInfoResponse.mobile(), userInfoResponse.email(), PROVIDER, null));

    }

    private NaverTokenIssueResponseDto getNaverAccessToken(
        String code, String state) {

        ResponseEntity<NaverTokenIssueResponseDto> accessTokenResponse = naverOAuthAdapter.getAccessToken(
            grant_type, clientId, clientSecret, code, state);

        return accessTokenResponse.getBody();
    }

    private NaverUserInfoResponseDto getNaverUserInfo(
        NaverTokenIssueResponseDto tokenResponse) {

        ResponseEntity<NaverInfoResponseDto> userInfoResponse = naverUserInfoAdapter.getNaverUserInfo(
            "Bearer " + " " + tokenResponse.access_token());

        return userInfoResponse.getBody().response();
    }

}
