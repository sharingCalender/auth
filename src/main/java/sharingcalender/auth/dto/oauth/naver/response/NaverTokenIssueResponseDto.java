package sharingcalender.auth.dto.oauth.naver.response;

public record NaverTokenIssueResponseDto(

    String access_token,

    String refresh_token,

    String token_type,

    int expires_in,

    String error,

    String error_description
){}
