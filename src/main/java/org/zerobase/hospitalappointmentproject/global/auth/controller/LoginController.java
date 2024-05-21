package org.zerobase.hospitalappointmentproject.global.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.global.auth.dto.LoginDto;
import org.zerobase.hospitalappointmentproject.global.auth.dto.LoginResponse;
import org.zerobase.hospitalappointmentproject.global.auth.security.JwtUtil;
import org.zerobase.hospitalappointmentproject.global.auth.service.LoginService;

@RestController
@RequiredArgsConstructor
public class LoginController {

  private final LoginService loginService;
  private final JwtUtil jwtUtil;

  @Value("${spring.jwt.time}")
  private Long expiredTime;

  @PostMapping("/login/patient")
  public ResponseEntity<?> patientLogin(LoginDto loginDto) {

    PatientEntity patient = loginService.PatientLogin(loginDto);

    String token = jwtUtil.createJwt(patient.getUsername(), patient.getRole(), expiredTime);

    return ResponseEntity.ok(new LoginResponse(patient.getUsername(), token));

  }

}
