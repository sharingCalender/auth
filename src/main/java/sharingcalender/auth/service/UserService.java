package sharingcalender.auth.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sharingcalender.auth.adapter.OriginUserAdapter;
import sharingcalender.auth.dto.MessageDto;
import sharingcalender.auth.dto.OriginUserDto;
import sharingcalender.auth.exception.OriginUserGetInfoFailException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final OriginUserAdapter originUserAdapter;

    public OriginUserDto getOriginUserInfo(String username) {

        try {
            ResponseEntity<OriginUserDto> originUserInfo = originUserAdapter.getOriginUserInfo(
                username);

            return originUserInfo.getBody();

        } catch (FeignException e) {

            throw new OriginUserGetInfoFailException("Get Origin User Info Fail");

        }


    }


}
