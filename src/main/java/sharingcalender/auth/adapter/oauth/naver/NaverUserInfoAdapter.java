package sharingcalender.auth.adapter.oauth.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import sharingcalender.auth.dto.oauth.response.OauthInfoResponseDto;

@FeignClient(name = "naver-user-info", url = "https://openapi.naver.com")
public interface NaverUserInfoAdapter {

    @GetMapping("/v1/nid/me")
    ResponseEntity<OauthInfoResponseDto> getNaverUserInfo(
        @RequestHeader("Authorization") String authorization);
}
