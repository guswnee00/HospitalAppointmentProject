package org.zerobase.hospitalappointmentproject.domain.staff.controller;

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
public class StaffController {

  private final StaffService staffService;

  @PostMapping("/signup/staff")
  public ResponseEntity<?> signup(@RequestBody StaffSignup.Request request) {
    StaffDto staffDto = staffService.signup(request);
    return ResponseEntity.ok(StaffSignup.Response.fromDto(staffDto));
  }

  @GetMapping("/staff/my-info")
  public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    StaffDto staffDto = staffService.getInfo(username);
    return ResponseEntity.ok(StaffInfoResponse.fromDto(staffDto));
  }

  @PatchMapping("/staff/my-info")
  public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody StaffInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    StaffDto staffDto = staffService.updateInfo(username, request);
    return ResponseEntity.ok(StaffInfoUpdate.Response.fromDto(staffDto));
  }

}
