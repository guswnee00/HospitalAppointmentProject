package org.zerobase.hospitalappointmentproject.domain.patient.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientDto;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientSignup;
import org.zerobase.hospitalappointmentproject.domain.patient.service.PatientService;

@RestController
@RequiredArgsConstructor
public class PatientController {

  private final PatientService patientService;

  @PostMapping("/signup/patient")
  public ResponseEntity<?> patientSignup(@RequestBody PatientSignup.Request request) {
    PatientDto patientDto = patientService.signup(request);
    return ResponseEntity.ok(PatientSignup.Response.fromDto(patientDto));
  }

}
