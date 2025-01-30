package sharingcalender.auth.adapter.oauth;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sharingcalender.auth.dto.oauth.naver.response.NaverTokenIssueResponseDto;


@FeignClient(name = "naver-oauth",url = "https://nid.naver.com")
public interface NaverOAuthAdapter {


    @GetMapping("/oauth2.0/token")
    ResponseEntity<NaverTokenIssueResponseDto> getAccessToken(
        @RequestParam("grant_type") String grantType,
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("code") String code,
        @RequestParam("state") String state
    );
}
