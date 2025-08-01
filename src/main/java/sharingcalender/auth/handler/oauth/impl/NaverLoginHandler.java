package sharingcalender.auth.handler.oauth.impl;

import feign.FeignException;
import java.net.URLEncoder;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import sharingcalender.auth.adapter.UserAdapter;
import sharingcalender.auth.adapter.oauth.naver.NaverOAuthAdapter;
import sharingcalender.auth.adapter.oauth.naver.NaverUserInfoAdapter;
import sharingcalender.auth.dto.oauth.OauthTokenDto;
import sharingcalender.auth.dto.oauth.OauthUserDto;
import sharingcalender.auth.dto.oauth.request.GetOauthUriRequestDto;
import sharingcalender.auth.dto.oauth.response.OauthInfoResponseDto;
import sharingcalender.auth.dto.oauth.naver.response.NaverTokenIssueResponseDto;
import sharingcalender.auth.dto.oauth.response.OauthUserInfoResponseDto;
import sharingcalender.auth.exception.AuthenticationException;
import sharingcalender.auth.exception.UnAuthorizedException;
import sharingcalender.auth.handler.oauth.OauthLoginHandler;
import sharingcalender.auth.repository.OauthTokenRepository;
import sharingcalender.auth.service.JwtTokenService;

@Component
public class NaverLoginHandler extends OauthLoginHandler {

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

    private final NaverOAuthAdapter naverOAuthAdapter;

    private final NaverUserInfoAdapter naverUserInfoAdapter;

    public NaverLoginHandler(JwtTokenService jwtTokenService, NaverOAuthAdapter naverOAuthAdapter,
        NaverUserInfoAdapter naverUserInfoAdapter, UserAdapter userAdapter,
        OauthTokenRepository oauthTokenRepository) {

        super(jwtTokenService, oauthTokenRepository, userAdapter);
        this.naverOAuthAdapter = naverOAuthAdapter;
        this.naverUserInfoAdapter = naverUserInfoAdapter;
    }

    @Override
    public String getOAuthUri(GetOauthUriRequestDto getOauthUriRequestDto) {
        String state = URLEncoder.encode(UUID.randomUUID().toString());

        return "redirect:" + UriComponentsBuilder.fromUriString(authorizationURI)
            .queryParam("response_type", responseType)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectURI)
            .queryParam("state", state)
            .build();
    }

    @Override
    protected OauthTokenDto getAccessToken(String code, String state) {
        try {

            ResponseEntity<NaverTokenIssueResponseDto> accessTokenResponse = naverOAuthAdapter.getAccessToken(
                grant_type, clientId, clientSecret, code, state);

            NaverTokenIssueResponseDto naverToken = accessTokenResponse.getBody();

            return OauthTokenDto.create(naverToken.access_token(), naverToken.refresh_token(),
                naverToken.token_type(), naverToken.expires_in());

        } catch (FeignException e) {
            throw new UnAuthorizedException("Naver Get Token Fail");
        }
    }

    @Override
    protected OauthUserDto getUserInfo(OauthTokenDto tokenResponse) {

        try {
            ResponseEntity<OauthInfoResponseDto> userInfoResponse = naverUserInfoAdapter.getNaverUserInfo(
                "Bearer " + " " + tokenResponse.access_token());

            OauthUserInfoResponseDto userInfo = userInfoResponse.getBody().response();

            return OauthUserDto.create(userInfo.id(), userInfo.name(), userInfo.mobile(),
                userInfo.email(), OauthType.NAVER.name());

        } catch (FeignException e) {
            throw new AuthenticationException("Naver Get User Info By AccessToken Fail");
        }

    }

    @Override
    public boolean isSupports(String provider) {

        return OauthType.NAVER.name().equals(provider.toUpperCase());
    }

}
