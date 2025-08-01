package sharingcalender.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;
import sharingcalender.auth.service.JwtTokenService;
import sharingcalender.auth.service.OauthService;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtTokenService jwtTokenService;

    private final OauthService oauthService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        if (!requestURI.matches("/api/auth/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer")) {

            log.debug("Logout request rejected: Missing or malformed Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String refreshToken = authorization.substring(7);

        if (!jwtTokenService.deleteRefreshToken(refreshToken)) {
            log.debug("Logout request rejected: Refresh Token Not Found In Redis");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        oauthService.deleteNaverTokenInRedis(refreshToken);


        response.setStatus(HttpServletResponse.SC_OK);

    }

}
