package sharingcalender.auth.dto.oauth.response;

public record OauthUserInfoResponseDto(
    String id,
    String name,
    String mobile,
    String email
){}
