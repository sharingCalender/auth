package sharingcalender.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sharingcalender.auth.dto.LoginRequestDto;
import sharingcalender.auth.dto.MessageDto;
import sharingcalender.auth.dto.TokenResponseDto;
import sharingcalender.auth.exception.UnAuthorizedException;
import sharingcalender.auth.service.JwtTokenService;

@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenService jwtTokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(),
                LoginRequestDto.class);

            String username = loginRequestDto.username();
            String password = loginRequestDto.password();

            return super.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        String username = authResult.getName();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // access 와 refresh 토큰 만들고 refresh 토큰 레디스에 저장하기

        TokenResponseDto tokenResponseDto = null;

        try {
            tokenResponseDto = jwtTokenService.issueToken(username, role);

        } catch (UnAuthorizedException e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(response.getOutputStream(), new MessageDto(e.getMessage()));
            return;
        }


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getOutputStream(), tokenResponseDto);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(),new MessageDto("로그인 실패"));

    }
}
