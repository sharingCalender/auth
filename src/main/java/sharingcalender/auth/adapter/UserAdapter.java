package sharingcalender.auth.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sharingcalender.auth.dto.UserOriginInfoResponseDto;
import sharingcalender.auth.dto.oauth.naver.request.OAuthUserIsExistRequestDto;

@FeignClient(value = "calender-service")
public interface UserAdapter {

    @GetMapping("/api/calender/user/{username}")
    ResponseEntity<UserOriginInfoResponseDto> getOriginUserInfo(@PathVariable("username") String username);

    @PostMapping("/api/calender/user/oauth/isExist")
    ResponseEntity<Void> oauthUserIsExist(
        @RequestBody OAuthUserIsExistRequestDto OAuthUserIsExistRequestDto);

}
