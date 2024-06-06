package org.zerobase.hospitalappointmentproject.domain.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientDto;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientInfoResponse;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientSignup;
import org.zerobase.hospitalappointmentproject.domain.patient.service.PatientService;
import org.zerobase.hospitalappointmentproject.global.auth.dto.PasswordRequest;

@RestController
@RequiredArgsConstructor
@Tag(name = "Patient API", description = "환자 API 입니다.")
public class PatientController {

  private final PatientService patientService;

  @Operation(summary = "patient signup", description = "환자가 개인 정보를 입력해 회원가입을 합니다.")
  @Parameter(name = "username", description = "아이디")
  @Parameter(name = "password", description = "비밀번호")
  @Parameter(name = "checkingPassword", description = "비밀번호 확인용")
  @Parameter(name = "name", description = "이름")
  @Parameter(name = "phoneNumber", description = "전화번호")
  @Parameter(name = "email", description = "이메일")
  @Parameter(name = "gender", description = "성별(M/F)")
  @Parameter(name = "address", description = "주소")
  @Parameter(name = "birthYear", description = "생년")
  @Parameter(name = "birthMonth", description = "생월")
  @Parameter(name = "birthDay", description = "생일")
  @PostMapping("/signup/patient")
  public ResponseEntity<?> patientSignup(@RequestBody PatientSignup.Request request) {
    PatientDto patientDto = patientService.signup(request);
    PatientSignup.Response response = PatientSignup.Response.fromDto(patientDto);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "patient get information", description = "환자가 개인 정보를 확인합니다.")
  @GetMapping("/patient/my-info")
  public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    PatientDto patientDto = patientService.getInfo(username);
    return ResponseEntity.ok(PatientInfoResponse.fromDto(patientDto));
  }

  @Operation(summary = "patient update information", description = "환자가 개인 정보를 수정합니다.")
  @Parameter(name = "currentPassword", description = "현재 비밀번호")
  @Parameter(name = "newPassword", description = "바꿀 비밀번호")
  @Parameter(name = "name", description = "이름")
  @Parameter(name = "phoneNumber", description = "전화번호")
  @Parameter(name = "email", description = "이메일")
  @Parameter(name = "gender", description = "성별(M/F)")
  @Parameter(name = "address", description = "주소")
  @Parameter(name = "birthYear", description = "생년")
  @Parameter(name = "birthMonth", description = "생월")
  @Parameter(name = "birthDay", description = "생일")
  @PatchMapping("/patient/my-info")
  public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody PatientInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    PatientDto patientDto = patientService.updateInfo(username, request);
    return ResponseEntity.ok(PatientInfoUpdate.Response.fromDto(patientDto));

  }

  @Operation(summary = "patient delete information", description = "환자가 개인 정보를 삭제합니다.")
  @Parameter(name = "password", description = "비밀번호")
  @DeleteMapping("/patient/my-info")
  public ResponseEntity<?> deleteInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody PasswordRequest request
  ) {
    String username = userDetails.getUsername();
    patientService.deleteInfo(username, request.getPassword());
    return ResponseEntity.ok("개인 정보 삭제가 완료되었습니다.");
  }

}
