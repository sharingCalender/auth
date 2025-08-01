package sharingcalender.auth.resolver;


import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sharingcalender.auth.dto.jwt.response.JwtTokenResponseDto;
import sharingcalender.auth.dto.oauth.request.GetOauthUriRequestDto;
import sharingcalender.auth.dto.oauth.request.OauthTokenRequestDto;
import sharingcalender.auth.exception.OauthLoginHandlerNotFoundException;
import sharingcalender.auth.handler.oauth.OauthLoginHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class OauthLoginResolver {

    private final List<OauthLoginHandler> oauthLoginHandlerList;

    public JwtTokenResponseDto getJwtToken(OauthTokenRequestDto oauthTokenRequestDto) {
        OauthLoginHandler handler = findHandler(oauthTokenRequestDto.provider());

        if (Objects.isNull(handler)) {
            log.error(
                "[OauthLoginResolver.getJwtToken] Can Not Find Handler, Oauth Login Fail, provider = {}",
                oauthTokenRequestDto.provider());

            throw new OauthLoginHandlerNotFoundException("Can Not Find Handler");
        }

        return handler.getJwtToken(oauthTokenRequestDto.code(), oauthTokenRequestDto.state());

    }

    public String getOauthUri(GetOauthUriRequestDto oauthUriRequestDto) {

        OauthLoginHandler handler = findHandler(oauthUriRequestDto.provider());

        if (Objects.isNull(handler)) {
            log.error(
                "[OauthLoginResolver.getOauthUri] Can Not Find Handler, Oauth Login Fail, provider = {}",
                oauthUriRequestDto.provider());

            throw new OauthLoginHandlerNotFoundException("Can Not Find Handler");
        }

        return handler.getOAuthUri(oauthUriRequestDto);
    }

    private OauthLoginHandler findHandler(String provider){
        return oauthLoginHandlerList.stream()
            .filter(handler -> handler.isSupports(provider))
            .findAny()
            .orElse(null);
    }


}
