package sharingcalender.auth.dto.oauth.naver.response;

public record NaverUserInfoResponseDto (
    String id,
    String name,
    String mobile,
    String email
){}
