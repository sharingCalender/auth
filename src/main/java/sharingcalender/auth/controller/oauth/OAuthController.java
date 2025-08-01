package sharingcalender.auth.controller.oauth;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import sharingcalender.auth.dto.jwt.response.JwtTokenResponseDto;
import sharingcalender.auth.dto.oauth.request.OauthTokenRequestDto;
import sharingcalender.auth.dto.oauth.request.GetOauthUriRequestDto;
import sharingcalender.auth.exception.BadRequestException;
import sharingcalender.auth.service.OauthService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth/oauth")
public class OAuthController {


    private final OauthService oauthService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> oauthLogin(
        @RequestBody GetOauthUriRequestDto getOauthUriRequestDto, BindingResult bindingResult) {

        String oAuth2CodeUrl = oauthService.getOAuthUri(getOauthUriRequestDto);

        Map<String, String> response = new HashMap<>();
        response.put("redirectURL", oAuth2CodeUrl);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/callback/redirect")
    public ResponseEntity<JwtTokenResponseDto> GetJwtToken(@RequestBody @Valid OauthTokenRequestDto oauthTokenRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Code Or State has Wrong Value");
        }

        JwtTokenResponseDto jwtToken = oauthService.getJwtToken(oauthTokenRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);

    }
}
