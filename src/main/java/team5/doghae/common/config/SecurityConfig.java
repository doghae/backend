package team5.doghae.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import team5.doghae.common.security.jwt.JwtAuthenticationEntryPoint;
import team5.doghae.common.security.jwt.JwtAuthenticationFilter;
import team5.doghae.common.security.jwt.JwtProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    private final String[] permitAlls = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/oauth2/login",
            "/oauth2/kakao/login",
            "/oauth2/google/login",
            "/oauth2/naver/login",
            "/oauth2/refresh",
            "/oauth2/expiredJwt",
            "/oauth2/test",
            "/health/server",
            "/health/profile",
            "/actuator/**",
            "/weather",
            "/api/**",
            "/"
    };

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * csrf, rememberMe, logout, formLogin, httpBasic 비활성화
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors  // CORS 설정을 구성
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호를 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP Basic 인증을 비활성화
                .sessionManagement(sessionManageMent -> sessionManageMent  // 세션 관리 설정을 구성
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션을 상태없음(stateless)으로 설정
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/static/**", "/index.html", "/oauth2/**", "/weather").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/post/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
                        .anyRequest().authenticated())
//                .exceptionHandling(exceptionHandling -> exceptionHandling  // 예외 처리 설정을 구성
//                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())  // 인증 실패 시의 엔트리 포인트를 설정
//                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, permitAlls), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationEntryPoint(objectMapper()), JwtAuthenticationFilter.class);

        return http.build();  // 보안 필터 체인을 빌드
    }

    static class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {  // 인증 실패 시의 동작을 정의하는 클래스입니다.

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException {

            response.setContentType("application/json");  // 응답의 콘텐츠 타입을 JSON으로 설정
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 응답 상태 코드를 403 (Forbidden)으로 설정
            response.getWriter().write("{ \"code\": \"NP\", \"message\": \"Do not have permission.\" }");  // 응답 본문에 JSON 형식의 에러 메시지를 씁니다.
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {  // CORS 설정을 구성하는 메서드입니다.
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Set-Cookie");

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
