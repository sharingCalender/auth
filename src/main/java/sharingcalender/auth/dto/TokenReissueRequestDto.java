package sharingcalender.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenReissueRequestDto (
    @NotBlank
    String refreshToken
){}
