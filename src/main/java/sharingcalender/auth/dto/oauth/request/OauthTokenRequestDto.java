package sharingcalender.auth.dto.oauth.request;

import jakarta.validation.constraints.NotBlank;

public record OauthTokenRequestDto(
    @NotBlank
    String code,

    @NotBlank
    String state,

    String provider
){}