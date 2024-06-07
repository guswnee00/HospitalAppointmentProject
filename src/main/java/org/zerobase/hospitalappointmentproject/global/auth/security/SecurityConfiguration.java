package org.zerobase.hospitalappointmentproject.global.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtUtil jwtUtil;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();

  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {

    return new BCryptPasswordEncoder();

  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // csrf disable
    http.csrf(AbstractHttpConfigurer::disable);

    // From 로그인 방식 disable
    http.formLogin(AbstractHttpConfigurer::disable);

    // http basic 인증 방식 disable
    http.httpBasic(AbstractHttpConfigurer::disable);

    // 경로별 인가 작업
    http.authorizeHttpRequests((auth) -> auth
        .requestMatchers("/", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()    // swagger
        .requestMatchers("/search/**", "/signup/**", "/login/**").permitAll()    // 회원가입, 로그인, 병원 검색 모두 허용
        .requestMatchers("/patient/**").hasRole("PATIENT")
        .requestMatchers("/doctor/**").hasRole("DOCTOR")
        .requestMatchers("/staff/**").hasRole("STAFF")
        .anyRequest().authenticated());

    // jwt 필터 추가
    http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

    // login 필터 추가
    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                                      UsernamePasswordAuthenticationFilter.class);

    // 세션 설정
    http.sessionManagement((session) -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();

  }

}
