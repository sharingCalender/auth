package sharingcalender.auth.dto.originuser.response;


import jakarta.validation.constraints.NotNull;

public record UserOriginInfoResponseDto(
    @NotNull
    String username,

    @NotNull
    String password
){}
