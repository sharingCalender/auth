package sharingcalender.auth.dto.oauth;

public record OauthTokenSaveParam(
    String access_token,

    String refresh_token,

    String token_type,

    int expires_in,

    String email


) {

    public static OauthTokenSaveParam create(OauthTokenDto token,
        OauthUserDto user) {

        return new OauthTokenSaveParam(token.access_token(), token.refresh_token(),
            token.token_type(), token.expires_in(), user.email());
    }

}
