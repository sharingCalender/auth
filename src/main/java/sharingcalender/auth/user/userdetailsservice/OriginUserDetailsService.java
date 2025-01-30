package sharingcalender.auth.user.userdetailsservice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sharingcalender.auth.dto.UserOriginInfoResponseDto;
import sharingcalender.auth.service.UserService;
import sharingcalender.auth.user.userdetails.OriginUserDetails;

@Service
@RequiredArgsConstructor
public class OriginUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserOriginInfoResponseDto originUserInfo = userService.getOriginUserInfo(username);

        return new OriginUserDetails(originUserInfo);
    }
}
