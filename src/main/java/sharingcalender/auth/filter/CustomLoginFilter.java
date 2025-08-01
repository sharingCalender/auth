package sharingcalender.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sharingcalender.auth.dto.LoginRequestDto;
import sharingcalender.auth.dto.exception.MessageDto;
import sharingcalender.auth.dto.jwt.response.JwtTokenResponseDto;
import sharingcalender.auth.exception.UnAuthorizedException;
import sharingcalender.auth.service.JwtTokenService;

@RequiredArgsConstructor
@Slf4j
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
            log.warn("Parsing Exception When Login : ", e);

            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {

        String username = authResult.getName();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // access 와 refresh 토큰 만들고 refresh 토큰 레디스에 저장하기

        JwtTokenResponseDto jwtTokenResponseDto = null;

        try {
            jwtTokenResponseDto = jwtTokenService.issueToken(username, role);

        } catch (UnAuthorizedException e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(response.getOutputStream(), new MessageDto(e.getMessage()));
            return;
        }


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getOutputStream(), jwtTokenResponseDto);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(),new MessageDto("로그인 실패"));

    }
}
