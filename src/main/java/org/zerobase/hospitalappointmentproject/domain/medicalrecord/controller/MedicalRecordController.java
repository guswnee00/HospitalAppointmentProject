package org.zerobase.hospitalappointmentproject.domain.medicalrecord.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordCreate;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordDto;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordUpdate;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.service.MedicalRecordService;

@RestController
@RequiredArgsConstructor
public class MedicalRecordController {

  private final MedicalRecordService medicalRecordService;

  @PostMapping("/doctor/medical-record")
  public ResponseEntity<?> create(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody MedicalRecordCreate.Request request
  ) {
    String username = userDetails.getUsername();
    MedicalRecordDto medicalRecordDto = medicalRecordService.create(request, username);
    return ResponseEntity.ok(MedicalRecordCreate.Response.fromDto(medicalRecordDto));
  }

  @PatchMapping("/doctor/medical-record/{medicalRecordId}")
  public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable Long medicalRecordId,
                                  @RequestBody MedicalRecordUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    MedicalRecordDto medicalRecordDto = medicalRecordService.update(request, username, medicalRecordId);
    return ResponseEntity.ok(MedicalRecordUpdate.Response.fromDto(medicalRecordDto));
  }

}
