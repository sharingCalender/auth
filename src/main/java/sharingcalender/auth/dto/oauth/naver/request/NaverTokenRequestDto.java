package sharingcalender.auth.dto.oauth.naver.request;

import jakarta.validation.constraints.NotBlank;

public record NaverTokenRequestDto(
    @NotBlank
    String code,

    @NotBlank
    String state
){}