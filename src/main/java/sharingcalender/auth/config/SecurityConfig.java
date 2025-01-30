package sharingcalender.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import sharingcalender.auth.filter.CustomLoginFilter;
import sharingcalender.auth.filter.CustomLogoutFilter;
import sharingcalender.auth.jwt.JwtUtil;
import sharingcalender.auth.service.JwtTokenService;
import sharingcalender.auth.user.userdetailsservice.OriginUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final OriginUserDetailsService originUserDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtTokenService jwtTokenService;

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)

            .formLogin(AbstractHttpConfigurer::disable)

            .httpBasic(AbstractHttpConfigurer::disable)

            .logout(AbstractHttpConfigurer::disable)

            .cors(AbstractHttpConfigurer::disable)

            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(request -> request
                .anyRequest().permitAll());

        http
            .addFilterAt(customLoginFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(customLogoutFilter(), LogoutFilter.class);

        return http.build();

    }

    @Bean
    public AuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(originUserDetailsService);

        return provider;
    }

    // singletonList() 는 불변 리스트를 생성해준다
    // List.of() 는 불변리스트지만 여러 요소가 존재할 수 있고
    // singletonList() 는 불변리스트이면서 하나의 요소만 존재할 수 있는 차이가 있다.
    @Bean
    public AuthenticationManager userAuthenticationManager() {
        return new ProviderManager(Collections.singletonList(userAuthenticationProvider()));
    }

    private CustomLoginFilter customLoginFilter() {
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(objectMapper, jwtTokenService);
        customLoginFilter.setAuthenticationManager(userAuthenticationManager());
        customLoginFilter.setFilterProcessesUrl("/api/auth/login");
        customLoginFilter.setPostOnly(true);

        return customLoginFilter;
    }

    private CustomLogoutFilter customLogoutFilter() {
        CustomLogoutFilter customLogoutFilter = new CustomLogoutFilter(jwtTokenService);

        return customLogoutFilter;
    }



}
