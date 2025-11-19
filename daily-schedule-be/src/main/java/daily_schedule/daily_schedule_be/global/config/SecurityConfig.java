package daily_schedule.daily_schedule_be.global.config;

import daily_schedule.daily_schedule_be.global.jwt.JwtFilter;
import daily_schedule.daily_schedule_be.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtProvider;

    private final String[] allowedUrls = {"/api/user/register", "/api/user" + "/login"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정 Bean 정의
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 개발 환경 출처 허용. Postman 테스트를 위해 모든 메서드/헤더 허용
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000",
                        "http://localhost:8080", "http://localhost:8080",
                        "http://127.0.0.1:8080"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtProvider);

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 1. CSRF 비활성화 (POST 요청 허용을 위해 필수)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 세션 비활성화 (JWT 사용 시 필수 설정)
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                // 폼 로그인 및 HTTP 기본 인증 비활성화 (API 서버의 기본 설정)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 요청에 대한 접근 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        // 회원가입 및 로그인 경로는 인증 없이 허용 (permitAll)
                        // /api/user/register, /api/user/login 경로는 POST 요청이므로 CSRF 비활성화가 필요함
                        .requestMatchers(allowedUrls).permitAll()
                        // 그 외 모든 요청은 인증 필요 (authenticated)
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}