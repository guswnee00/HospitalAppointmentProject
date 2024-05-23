package org.zerobase.hospitalappointmentproject.global.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerobase.hospitalappointmentproject.global.auth.dto.CustomUserDetails;
import org.zerobase.hospitalappointmentproject.global.auth.entity.UserEntity;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException
  {

    // request 에서 헤더 찾기
    String authorization = request.getHeader("Authorization");
    // 헤더 검증
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      // 조건이 해당되면 종료
      return;

    }

    // Bearer 제거
    String token = authorization.split(" ")[1];
    // 토큰 소멸 시간 검증
    if (jwtUtil.isExpired(token)) {
      filterChain.doFilter(request, response);
      // 조건이 해당하면 종료
      return;
    }

    // 토큰에서 획득
    String username = jwtUtil.getUsername(token);
    String role = jwtUtil.getRole(token);

    // 값 setting
    UserEntity userEntity = UserEntity.builder()
                                      .username(username)
                                      .password("PASSword1234")
                                      .role(role)
                                      .build();

    CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

    // 인증 토큰 생성
    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    // 세션에 사용자 등록
    SecurityContextHolder.getContext().setAuthentication(authToken);
    // 다음 필터 실행
    filterChain.doFilter(request, response);


  }
}
