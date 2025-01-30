package sharingcalender.auth.dto;

public record TokenResponseDto (

    String accessToken,

    String refreshToken

){}
