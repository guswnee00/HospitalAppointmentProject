package org.zerobase.hospitalappointmentproject.domain.medicalrecord.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/doctor/medical-record")
  public ResponseEntity<?> doctorMedicalRecords(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam(required = false, defaultValue = "consultationDate") String sortBy,
                                                @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                @PageableDefault Pageable pageable
  ) {
    String username = userDetails.getUsername();
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable sortPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
    Page<MedicalRecordDto> medicalRecords = medicalRecordService.doctorMedicalRecords(username, sortPage);
    return ResponseEntity.ok(medicalRecords);
  }

  @GetMapping("/patient/medical-record")
  public ResponseEntity<?> patientMedicalRecords(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam(required = false, defaultValue = "consultationDate") String sortBy,
                                                 @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                 @PageableDefault Pageable pageable
  ) {
    String username = userDetails.getUsername();
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable sortPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
    Page<MedicalRecordDto> medicalRecords = medicalRecordService.patientMedicalRecords(username, sortPage);
    return ResponseEntity.ok(medicalRecords);
  }

}
