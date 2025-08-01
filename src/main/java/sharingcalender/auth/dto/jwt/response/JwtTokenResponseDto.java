package sharingcalender.auth.dto.jwt.response;

public record JwtTokenResponseDto(

    String accessToken,

    String refreshToken

){}
