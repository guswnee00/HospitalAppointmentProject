package org.zerobase.hospitalappointmentproject.domain.appointment.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentCreate;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentDto;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentUpdate;
import org.zerobase.hospitalappointmentproject.domain.appointment.service.AppointmentService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Appointment API", description = "예약 API 입니다.")
public class AppointmentController {

  private final AppointmentService appointmentService;

  @Operation(summary = "appointment create", description = "환자가 예약을 생성 합니다.")
  @Parameter(name = "hospitalName", description = "진료 받을 병원 이름")
  @Parameter(name = "specialtyName", description = "진료 받을 과목 이름")
  @Parameter(name = "doctorName", description = "진료 받을 의사 이름")
  @Parameter(name = "appointmentDate", description = "예약 날짜")
  @Parameter(name = "appointmentHour", description = "예약 시")
  @Parameter(name = "appointmentMinute", description = "예약 분")
  @PostMapping("/patient/my-appointment")
  public ResponseEntity<?> create(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody AppointmentCreate.Request request
  ) {
    String username = userDetails.getUsername();
    AppointmentDto appointmentDto = appointmentService.create(request, username);
    return ResponseEntity.ok(AppointmentCreate.Response.fromDto(appointmentDto));
  }

  @Operation(summary = "appointment update", description = "환자가 예약을 수정 합니다.")
  @Parameter(name = "hospitalName", description = "진료 받을 병원 이름")
  @Parameter(name = "specialtyName", description = "진료 받을 과목 이름")
  @Parameter(name = "doctorName", description = "진료 받을 의사 이름")
  @Parameter(name = "appointmentDate", description = "예약 날짜")
  @Parameter(name = "appointmentHour", description = "예약 시")
  @Parameter(name = "appointmentMinute", description = "예약 분")
  @PatchMapping("/patient/my-appointment/{appointmentId}")
  public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails,
                                  @Parameter(name = "appointmentId", description = "예약 번호")
                                  @PathVariable Long appointmentId,
                                  @RequestBody AppointmentUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    AppointmentDto appointmentDto = appointmentService.update(request, appointmentId, username);
    return ResponseEntity.ok(AppointmentUpdate.Response.fromDto(appointmentDto));
  }

  @Operation(summary = "appointment cancel", description = "환자가 예약을 취소 합니다.")
  @DeleteMapping("/patient/my-appointment/{appointmentId}")
  public ResponseEntity<?> cancel(@AuthenticationPrincipal UserDetails userDetails,
                                  @Parameter(name = "appointmentId", description = "예약 번호")
                                  @PathVariable Long appointmentId
  ) {
    String username = userDetails.getUsername();
    appointmentService.cancel(username, appointmentId);
    return ResponseEntity.ok("예약 취소가 완료되었습니다.");
  }

  @Operation(summary = "appointment page for patient", description = "환자가 예약들을 확인 합니다.")
  @GetMapping("/patient/my-appointments")
  public ResponseEntity<?> patientAppointments(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestParam(required = false, defaultValue = "appointmentDate") String sortBy,
                                               @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                               @PageableDefault Pageable pageable
  ) {
    String username = userDetails.getUsername();
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable sortPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
    Page<AppointmentDto> appointments = appointmentService.patientAppointments(username, sortPage);
    return ResponseEntity.ok(appointments);
  }

  @Operation(summary = "appointment page for staff", description = "병원 관계자가 예약들을 확인 합니다.")
  @GetMapping("/staff/my-hospital-appointments")
  public ResponseEntity<?> staffAppointments(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestParam(required = false, defaultValue = "appointmentDate") String sortBy,
                                             @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                             @PageableDefault Pageable pageable
  ) {
    String username = userDetails.getUsername();
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable sortPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
    Page<AppointmentDto> appointments = appointmentService.staffAppointments(username, sortPage);
    return ResponseEntity.ok(appointments);
  }

  @Operation(summary = "appointment page for doctor", description = "의사가 예약들을 확인 합니다.")
  @GetMapping("/doctor/my-appointments")
  public ResponseEntity<?> doctorAppointments(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam(required = false, defaultValue = "appointmentDate") String sortBy,
                                              @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                              @PageableDefault Pageable pageable
  ) {
    String username = userDetails.getUsername();
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable sortPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
    Page<AppointmentDto> appointments = appointmentService.doctorAppointments(username, sortPage);
    return ResponseEntity.ok(appointments);
  }

  @Operation(summary = "patient arrival check", description = "환자가 예약 당일 15분 전에 도착했는지 확인합니다.")
  @PostMapping("/patient/check-arrival/{appointmentId}")
  public ResponseEntity<?> patientArrival(@AuthenticationPrincipal UserDetails userDetails,
                                          @Parameter(name = "appointmentId", description = "예약 번호")
                                          @PathVariable Long appointmentId
  ) {
    String username = userDetails.getUsername();
    appointmentService.patientArrival(username, appointmentId);
    return ResponseEntity.ok("도착이 확인되었습니다.");
  }

}
