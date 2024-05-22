package org.zerobase.hospitalappointmentproject.domain.doctor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorSignup;
import org.zerobase.hospitalappointmentproject.domain.doctor.service.DoctorService;

@RestController
@RequiredArgsConstructor
public class DoctorController {

  private final DoctorService doctorService;

  @PostMapping("/signup/doctor")
  public ResponseEntity<?> doctorSignup(@RequestBody DoctorSignup.Request request) {
    DoctorDto doctorDto = doctorService.signup(request);
    return ResponseEntity.ok(DoctorSignup.Response.fromDto(doctorDto));
  }

}
