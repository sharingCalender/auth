package sharingcalender.auth.adapter.oauth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import sharingcalender.auth.dto.oauth.naver.response.NaverInfoResponseDto;
import sharingcalender.auth.dto.oauth.naver.response.NaverUserInfoResponseDto;

@FeignClient(name = "naver-user-info", url = "https://openapi.naver.com")
public interface NaverUserInfoAdapter {

    @GetMapping("/v1/nid/me")
    ResponseEntity<NaverInfoResponseDto> getNaverUserInfo(
        @RequestHeader("Authorization") String authorization);
}
