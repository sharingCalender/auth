package sharingcalender.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import sharingcalender.auth.dto.TokenResponseDto;
import sharingcalender.auth.exception.BadRequestException;
import sharingcalender.auth.service.JwtTokenService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth/user")
public class UserController {

    private final JwtTokenService jwtTokenService;

    @PostMapping("/accessToken/reissue")
    public ResponseEntity<TokenResponseDto> reissueToken(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {

        if (refreshToken.isBlank()) {
            throw new BadRequestException("Refresh Token Is Not Valid");
        }

        TokenResponseDto tokenResponseDto = jwtTokenService.reissueToken(refreshToken.substring(7));

        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponseDto);
    }

}
