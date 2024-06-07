package org.zerobase.hospitalappointmentproject.domain.doctor.controller;

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
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorInfoResponse;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorSignup;
import org.zerobase.hospitalappointmentproject.domain.doctor.service.DoctorService;
import org.zerobase.hospitalappointmentproject.global.auth.dto.PasswordRequest;

@RestController
@RequiredArgsConstructor
@Tag(name = "Doctor API", description = "의사 API 입니다.")
public class DoctorController {

  private final DoctorService doctorService;

  @Operation(summary = "doctor signup", description = "의사가 개인 정보를 입력해 회원가입을 합니다.")
  @Parameter(name = "username", description = "아이디")
  @Parameter(name = "password", description = "비밀번호")
  @Parameter(name = "checkingPassword", description = "비밀번호 확인용")
  @Parameter(name = "name", description = "이름")
  @Parameter(name = "phoneNumber", description = "전화번호")
  @Parameter(name = "email", description = "이메일")
  @Parameter(name = "bio", description = "이력")
  @Parameter(name = "specialtyName", description = "전공(진료)과목")
  @Parameter(name = "hospitalName", description = "근무하는 병원 이름")
  @PostMapping("/signup/doctor")
  public ResponseEntity<?> signup(@RequestBody DoctorSignup.Request request) {
    DoctorDto doctorDto = doctorService.signup(request);
    return ResponseEntity.ok(DoctorSignup.Response.fromDto(doctorDto));
  }

  @Operation(summary = "doctor get information", description = "의사가 개인 정보를 확인합니다.")
  @GetMapping("/doctor/my-info")
  public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    DoctorDto doctorDto = doctorService.getInfo(username);
    return ResponseEntity.ok(DoctorInfoResponse.fromDto(doctorDto));
  }

  @Operation(summary = "doctor update information", description = "의사가 개인 정보를 수정합니다.")
  @Parameter(name = "currentPassword", description = "현재 비밀번호")
  @Parameter(name = "newPassword", description = "바꿀 비밀번호")
  @Parameter(name = "name", description = "이름")
  @Parameter(name = "phoneNumber", description = "전화번호")
  @Parameter(name = "email", description = "이메일")
  @Parameter(name = "bio", description = "이력")
  @PatchMapping("/doctor/my-info")
  public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody DoctorInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    DoctorDto doctorDto = doctorService.updateInfo(username, request);
    return ResponseEntity.ok(DoctorInfoUpdate.Response.fromDto(doctorDto));
  }

  @Operation(summary = "doctor delete information", description = "의사가 개인 정보를 삭제합니다.")
  @Parameter(name = "password", description = "비밀번호")
  @DeleteMapping("/doctor/my-info")
  public ResponseEntity<?> deleteInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody PasswordRequest request
  ) {
    String username = userDetails.getUsername();
    doctorService.deleteInfo(username, request.getPassword());
    return ResponseEntity.ok("개인 정보 삭제가 완료되었습니다.");
  }

}
