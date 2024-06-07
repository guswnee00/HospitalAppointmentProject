package org.zerobase.hospitalappointmentproject.domain.medicalrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.zerobase.hospitalappointmentproject.global.common.MedicalRecordSortBy;

@RestController
@RequiredArgsConstructor
@Tag(name = "Medical Record API", description = "진료 기록 API 입니다.")
public class MedicalRecordController {

  private final MedicalRecordService medicalRecordService;

  @Operation(summary = "medical record create", description = "의사가 진료기록을 생성합니다.")
  @Parameter(name = "patientName", description = "진료 받은 환자 이름")
  @Parameter(name = "patientBirthDate", description = "진료 받은 환자 생년월일")
  @Parameter(name = "doctorName", description = "진료한 의사 이름")
  @Parameter(name = "consultationDate", description = "진료 날짜")
  @Parameter(name = "diagnosis", description = "진단명")
  @Parameter(name = "treatment", description = "치료법")
  @Parameter(name = "prescription", description = "처방전")
  @PostMapping("/doctor/medical-record")
  public ResponseEntity<?> create(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody MedicalRecordCreate.Request request
  ) {
    String username = userDetails.getUsername();
    MedicalRecordDto medicalRecordDto = medicalRecordService.create(request, username);
    return ResponseEntity.ok(MedicalRecordCreate.Response.fromDto(medicalRecordDto));
  }

  @Operation(summary = "medical record update", description = "의사가 진료기록을 수정합니다.")
  @Parameter(name = "diagnosis", description = "진단명")
  @Parameter(name = "treatment", description = "치료법")
  @Parameter(name = "prescription", description = "처방전")
  @PatchMapping("/doctor/medical-record/{medicalRecordId}")
  public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails,
                                  @Parameter(name = "medicalRecordId", description = "진료기록 번호")
                                  @PathVariable Long medicalRecordId,
                                  @RequestBody MedicalRecordUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    MedicalRecordDto medicalRecordDto = medicalRecordService.update(request, username, medicalRecordId);
    return ResponseEntity.ok(MedicalRecordUpdate.Response.fromDto(medicalRecordDto));
  }

  @Operation(summary = "list of medical record for doctor", description = "의사가 작성한 진료기록들을 확인합니다.")
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

  @Operation(summary = "list of medical record for patient", description = "환자가 본인의 진료기록들을 확인합니다.")
  @GetMapping("/patient/medical-record")
  public ResponseEntity<?> patientMedicalRecords(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam(required = false, defaultValue = "CONSULTATION_DATE") String sortBy,
                                                 @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                 @PageableDefault Pageable pageable
  ) {
    String username = userDetails.getUsername();
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    MedicalRecordSortBy medicalRecordSortBy = MedicalRecordSortBy.fromField(sortBy);
    Pageable sortPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, medicalRecordSortBy.getField()));
    Page<MedicalRecordDto> medicalRecords = medicalRecordService.patientMedicalRecords(username, sortPage);
    return ResponseEntity.ok(medicalRecords);
  }

}
