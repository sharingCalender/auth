package sharingcalender.auth.dto;


import jakarta.validation.constraints.NotNull;

public record UserOriginInfoResponseDto(
    @NotNull
    String username,

    @NotNull
    String password
){}
