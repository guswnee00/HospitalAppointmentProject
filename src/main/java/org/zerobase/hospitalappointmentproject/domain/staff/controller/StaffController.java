package org.zerobase.hospitalappointmentproject.domain.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffDto;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffSignup;
import org.zerobase.hospitalappointmentproject.domain.staff.service.StaffService;

@RestController
@RequiredArgsConstructor
public class StaffController {

  private final StaffService staffService;

  @PostMapping("/signup/staff")
  public ResponseEntity<?> staffSignup(@RequestBody StaffSignup.Request request) {
    StaffDto staffDto = staffService.signup(request);
    return ResponseEntity.ok(StaffSignup.Response.fromDto(staffDto));
  }

}
