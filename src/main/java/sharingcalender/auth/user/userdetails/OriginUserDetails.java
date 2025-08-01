package sharingcalender.auth.user.userdetails;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sharingcalender.auth.dto.originuser.response.UserOriginInfoResponseDto;


public class OriginUserDetails implements UserDetails {

    private final UserOriginInfoResponseDto userOriginInfoResponseDto;

    public OriginUserDetails(UserOriginInfoResponseDto userOriginInfoResponseDto) {
        this.userOriginInfoResponseDto = userOriginInfoResponseDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> "USER");

        return collection;
    }

    @Override
    public String getPassword() {
        return userOriginInfoResponseDto.password();
    }

    @Override
    public String getUsername() {
        return userOriginInfoResponseDto.username();
    }


}
