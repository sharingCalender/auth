package sharingcalender.auth.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sharingcalender.auth.dto.OriginUserDto;

@FeignClient(value = "calender-service")
public interface OriginUserAdapter {

    @GetMapping("/api/calender/originUser/{username}")
    ResponseEntity<OriginUserDto> getOriginUserInfo(@PathVariable("username") String username);

}
