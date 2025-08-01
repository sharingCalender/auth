package sharingcalender.auth.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sharingcalender.auth.adapter.UserAdapter;
import sharingcalender.auth.dto.originuser.response.UserOriginInfoResponseDto;
import sharingcalender.auth.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter userAdapter;

    public UserOriginInfoResponseDto getOriginUserInfo(String username) {

        try {
            ResponseEntity<UserOriginInfoResponseDto> originUserInfo = userAdapter.getOriginUserInfo(
                username);

            return originUserInfo.getBody();

        } catch (FeignException e) {

            throw new ResourceNotFoundException("Get Origin User Info Fail");

        }


    }


}
