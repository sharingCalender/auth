package sharingcalender.auth.dto.oauth;

public record OauthUserDto(
    String id,
    String name,
    String mobile,
    String email,
    String provider
) {

    public static OauthUserDto create(String id, String name, String mobile, String email,String provider) {
        return new OauthUserDto(id, name, mobile, email,provider);
    }
}
