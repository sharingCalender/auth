package sharingcalender.auth.dto.oauth;

public record OauthTokenDto(

    String access_token,

    String refresh_token,

    String token_type,

    int expires_in
) {

    public static OauthTokenDto create(String access_token, String refresh_token, String token_type,
        int expires_in) {
        return new OauthTokenDto(access_token, refresh_token, token_type, expires_in);
    }
}
