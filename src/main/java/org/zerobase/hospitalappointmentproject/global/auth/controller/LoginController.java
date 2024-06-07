package org.zerobase.hospitalappointmentproject.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.global.auth.dto.LoginDto;
import org.zerobase.hospitalappointmentproject.global.auth.dto.LoginResponse;
import org.zerobase.hospitalappointmentproject.global.auth.security.JwtUtil;
import org.zerobase.hospitalappointmentproject.global.auth.service.LoginService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Login API", description = "로그인 API 입니다.")
public class LoginController {

  private final LoginService loginService;
  private final JwtUtil jwtUtil;

  @Value("${spring.jwt.time}")
  private Long expiredTime;

  @Operation(summary = "patient login", description = "환자가 로그인을 합니다.")
  @Parameter(name = "username", description = "아이디")
  @Parameter(name = "password", description = "비밀번호")
  @PostMapping("/login/patient")
  public ResponseEntity<?> patientLogin(@RequestBody LoginDto loginDto) {

    PatientEntity patient = loginService.patientLogin(loginDto);

    String token = jwtUtil.createJwt(patient.getUsername(), patient.getRole(), expiredTime);

    return ResponseEntity.ok(new LoginResponse(patient.getUsername(), token));

  }

  @Operation(summary = "doctor login", description = "의사가 로그인을 합니다.")
  @Parameter(name = "username", description = "아이디")
  @Parameter(name = "password", description = "비밀번호")
  @PostMapping("/login/doctor")
  public ResponseEntity<?> doctorLogin(@RequestBody LoginDto loginDto) {

    DoctorEntity doctor = loginService.doctorLogin(loginDto);

    String token = jwtUtil.createJwt(doctor.getUsername(), doctor.getRole(), expiredTime);

    return ResponseEntity.ok(new LoginResponse(doctor.getUsername(), token));

  }

  @Operation(summary = "staff login", description = "병원 관계자가 로그인을 합니다.")
  @Parameter(name = "username", description = "아이디")
  @Parameter(name = "password", description = "비밀번호")
  @PostMapping("/login/staff")
  public ResponseEntity<?> staffLogin(@RequestBody LoginDto loginDto) {

    StaffEntity staff = loginService.staffLogin(loginDto);

    String token = jwtUtil.createJwt(staff.getUsername(), staff.getRole(), expiredTime);

    return ResponseEntity.ok(new LoginResponse(staff.getUsername(), token));

  }

}
