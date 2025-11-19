package daily_schedule.daily_schedule_be.global.jwt;

import jakarta.servlet.FilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // HTTP 요청 헤더에서 JWT 토큰을 추출하는 함수
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // "Bearer "로 시작하는지 확인하고, 토큰 값만 추출
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. HTTP 요청에서 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰이 유효한지 검증
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // 3. 토큰에서 사용자 ID 추출
            String userId = jwtTokenProvider.getSubject(token);

            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            UserDetails principal = new User(userId, "", authorities);

            // 4. Spring Security Authentication 객체 생성
            // 여기서는 권한을 'ROLE_USER'로 고정하고, 비밀번호는 null로 처리 (이미 토큰으로 인증되었기 때문)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principal, // Principal (사용자 ID)
                    null,   // Credentials (비밀번호는 JWT 인증 방식에서는 필요 없음)
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // 권한 부여
            );

            // 5. SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}