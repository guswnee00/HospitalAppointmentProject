package org.zerobase.hospitalappointmentproject.domain.patient.controller;

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
public class PatientController {

  private final PatientService patientService;

  @PostMapping("/signup/patient")
  public ResponseEntity<?> patientSignup(@RequestBody PatientSignup.Request request) {
    PatientDto patientDto = patientService.signup(request);
    PatientSignup.Response response = PatientSignup.Response.fromDto(patientDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/patient/my-info")
  public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    PatientDto patientDto = patientService.getInfo(username);
    return ResponseEntity.ok(PatientInfoResponse.fromDto(patientDto));
  }

  @PatchMapping("/patient/my-info")
  public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody PatientInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    PatientDto patientDto = patientService.updateInfo(username, request);
    return ResponseEntity.ok(PatientInfoUpdate.Response.fromDto(patientDto));

  }

  @DeleteMapping("/patient/my-info")
  public ResponseEntity<?> deleteInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody PasswordRequest request
  ) {
    String username = userDetails.getUsername();
    patientService.deleteInfo(username, request.getPassword());
    return ResponseEntity.ok("개인 정보 삭제가 완료되었습니다.");
  }

}