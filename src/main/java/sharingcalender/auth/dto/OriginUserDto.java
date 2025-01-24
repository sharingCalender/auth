package sharingcalender.auth.dto;


import jakarta.validation.constraints.NotNull;

public record OriginUserDto (
    @NotNull
    String username,

    @NotNull
    String password
){}
