package sharingcalender.auth.user.userdetails;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sharingcalender.auth.dto.OriginUserDto;


public class OriginUserDetails implements UserDetails {

    private final OriginUserDto originUserDto;

    public OriginUserDetails(OriginUserDto originUserDto) {
        this.originUserDto = originUserDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> "USER");

        return collection;
    }

    @Override
    public String getPassword() {
        return originUserDto.password();
    }

    @Override
    public String getUsername() {
        return originUserDto.username();
    }


}
