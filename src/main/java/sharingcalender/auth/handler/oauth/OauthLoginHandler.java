package sharingcalender.auth.handler.oauth;


import feign.FeignException;
import sharingcalender.auth.adapter.UserAdapter;
import sharingcalender.auth.dto.jwt.response.JwtTokenResponseDto;
import sharingcalender.auth.dto.oauth.OauthTokenDto;
import sharingcalender.auth.dto.oauth.OauthTokenSaveParam;
import sharingcalender.auth.dto.oauth.OauthUserDto;
import sharingcalender.auth.dto.oauth.request.OAuthUserIsExistRequestDto;
import sharingcalender.auth.dto.oauth.request.GetOauthUriRequestDto;
import sharingcalender.auth.exception.BadRequestException;
import sharingcalender.auth.repository.OauthTokenRepository;
import sharingcalender.auth.service.JwtTokenService;



public abstract class OauthLoginHandler {

    private final JwtTokenService jwtTokenService;
    private final OauthTokenRepository oauthTokenRepository;
    private final UserAdapter userAdapter;

    public OauthLoginHandler(JwtTokenService jwtTokenService,
        OauthTokenRepository oauthTokenRepository, UserAdapter userAdapter) {

        this.jwtTokenService = jwtTokenService;
        this.oauthTokenRepository = oauthTokenRepository;
        this.userAdapter = userAdapter;
    }

    public abstract String getOAuthUri(GetOauthUriRequestDto getOauthUriRequestDto);

    public JwtTokenResponseDto getJwtToken(String code, String state) {

        // access token 발급하기
        OauthTokenDto oauthToken = getAccessToken(code, state);

        // user info 가져오기
        OauthUserDto oauthUser = getUserInfo(oauthToken);

        // 기존회원인지 아닌지 여부 확인하고 가입처리
        oauthUserIsExist(oauthUser);

        // naver token info redis 저장
        saveTokenInRedis(oauthToken,oauthUser);

        //jwt 토큰 발급하고 리턴해서 프런트로 넘기기

        return jwtTokenService.issueToken(oauthUser.email(), "USER");

    }

    private void saveTokenInRedis(OauthTokenDto tokenResponse,OauthUserDto userInfoResponse){

        oauthTokenRepository.saveTokenInRedis(
            OauthTokenSaveParam.create(tokenResponse, userInfoResponse));

    }

    private void oauthUserIsExist(OauthUserDto user) {
        try {
            userAdapter.oauthUserIsExist(
                new OAuthUserIsExistRequestDto(user.id(), user.name(),
                    user.mobile(), user.email(), user.provider(), null));

        } catch (FeignException e) {
            throw new BadRequestException("UserIsExist Check Fail");
        }
    }

    protected abstract OauthTokenDto getAccessToken(
        String code, String state);

    protected abstract OauthUserDto getUserInfo(
        OauthTokenDto tokenResponse);

    public abstract boolean isSupports(String provider);

    protected enum OauthType {
        NAVER("NAVER");

        private String name;


        OauthType(String name){
            this.name = name;
        }

    }

}
