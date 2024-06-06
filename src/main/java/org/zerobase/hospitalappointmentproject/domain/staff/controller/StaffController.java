package org.zerobase.hospitalappointmentproject.domain.staff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffDto;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffInfoResponse;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffSignup;
import org.zerobase.hospitalappointmentproject.domain.staff.service.StaffService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Staff API", description = "병원 관계자 API 입니다.")
public class StaffController {

  private final StaffService staffService;

  @Operation(summary = "staff signup", description = "병원 관계자가 개인 정보를 입력해 회원가입을 합니다.")
  @Parameter(name = "username", description = "아이디")
  @Parameter(name = "password", description = "비밀번호")
  @Parameter(name = "checkingPassword", description = "비밀번호 확인용")
  @Parameter(name = "name", description = "이름")
  @Parameter(name = "phoneNumber", description = "전화번호")
  @Parameter(name = "email", description = "이메일")
  @PostMapping("/signup/staff")
  public ResponseEntity<?> signup(@RequestBody StaffSignup.Request request) {
    StaffDto staffDto = staffService.signup(request);
    return ResponseEntity.ok(StaffSignup.Response.fromDto(staffDto));
  }

  @Operation(summary = "staff get information", description = "병원 관계자가 개인 정보를 확인합니다.")
  @GetMapping("/staff/my-info")
  public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    StaffDto staffDto = staffService.getInfo(username);
    return ResponseEntity.ok(StaffInfoResponse.fromDto(staffDto));
  }

  @Operation(summary = "staff update information", description = "병원 관계자가 개인 정보를 수정합니다.")
  @Parameter(name = "currentPassword", description = "현재 비밀번호")
  @Parameter(name = "newPassword", description = "바꿀 비밀번호")
  @Parameter(name = "name", description = "이름")
  @Parameter(name = "phoneNumber", description = "전화번호")
  @Parameter(name = "email", description = "이메일")
  @PatchMapping("/staff/my-info")
  public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody StaffInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    StaffDto staffDto = staffService.updateInfo(username, request);
    return ResponseEntity.ok(StaffInfoUpdate.Response.fromDto(staffDto));
  }

}
