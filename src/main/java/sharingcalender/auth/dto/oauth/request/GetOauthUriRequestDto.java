package sharingcalender.auth.dto.oauth.request;

import jakarta.validation.constraints.NotBlank;

public record GetOauthUriRequestDto(

    @NotBlank
    String provider
){}
