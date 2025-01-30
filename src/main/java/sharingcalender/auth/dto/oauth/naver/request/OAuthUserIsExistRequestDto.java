package sharingcalender.auth.dto.oauth.naver.request;

import jakarta.validation.constraints.NotBlank;

public record OAuthUserIsExistRequestDto(
    @NotBlank
    String id,

    @NotBlank
    String name,

    @NotBlank
    String mobile,

    @NotBlank
    String email,

    @NotBlank
    String provider,

    String password
){}
