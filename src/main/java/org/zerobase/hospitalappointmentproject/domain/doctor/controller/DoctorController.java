package org.zerobase.hospitalappointmentproject.domain.doctor.controller;

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
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorInfoResponse;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorSignup;
import org.zerobase.hospitalappointmentproject.domain.doctor.service.DoctorService;
import org.zerobase.hospitalappointmentproject.global.auth.dto.PasswordRequest;

@RestController
@RequiredArgsConstructor
public class DoctorController {

  private final DoctorService doctorService;

  @PostMapping("/signup/doctor")
  public ResponseEntity<?> signup(@RequestBody DoctorSignup.Request request) {
    DoctorDto doctorDto = doctorService.signup(request);
    return ResponseEntity.ok(DoctorSignup.Response.fromDto(doctorDto));
  }

  @GetMapping("/doctor/my-info")
  public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    DoctorDto doctorDto = doctorService.getInfo(username);
    return ResponseEntity.ok(DoctorInfoResponse.fromDto(doctorDto));
  }

  @PatchMapping("/doctor/my-info")
  public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody DoctorInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    DoctorDto doctorDto = doctorService.updateInfo(username, request);
    return ResponseEntity.ok(DoctorInfoUpdate.Response.fromDto(doctorDto));
  }

  @DeleteMapping("/doctor/my-info")
  public ResponseEntity<?> deleteInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody PasswordRequest request
  ) {
    String username = userDetails.getUsername();
    doctorService.deleteInfo(username, request.getPassword());
    return ResponseEntity.ok("개인 정보 삭제가 완료되었습니다.");
  }

}
