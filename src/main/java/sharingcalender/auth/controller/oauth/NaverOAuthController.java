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

import sharingcalender.auth.dto.TokenResponseDto;
import sharingcalender.auth.dto.oauth.naver.response.NaverTokenIssueResponseDto;
import sharingcalender.auth.dto.oauth.naver.request.NaverTokenRequestDto;
import sharingcalender.auth.dto.oauth.naver.response.NaverUserInfoResponseDto;
import sharingcalender.auth.exception.BadRequestException;
import sharingcalender.auth.service.NaverOAuthService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth/oauth")
public class NaverOAuthController {

    private final NaverOAuthService naverOAuthService;


    @PostMapping("/naver/login")
    public ResponseEntity<Map<String, String>> naverOauthLogin() {

        String oAuth2CodeUrl = naverOAuthService.getOAuth2CodeUrl();
        Map<String, String> response = new HashMap<>();
        response.put("redirectURL", "redirect:" + oAuth2CodeUrl);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/naver/callback/redirect")
    public ResponseEntity<TokenResponseDto> naverGetToken(@RequestBody @Valid NaverTokenRequestDto naverTokenRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Code Or State has Wrong Value");
        }

        TokenResponseDto jwtToken = naverOAuthService.getJwtToken(
            naverTokenRequestDto.code(),
            naverTokenRequestDto.state());

        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);

    }
}
