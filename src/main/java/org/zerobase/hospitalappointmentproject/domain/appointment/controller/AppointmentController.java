package org.zerobase.hospitalappointmentproject.domain.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentCreate;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentDto;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentUpdate;
import org.zerobase.hospitalappointmentproject.domain.appointment.service.AppointmentService;

@RestController
@RequiredArgsConstructor
public class AppointmentController {

  private final AppointmentService appointmentService;

  @PostMapping("/patient/my-appointment")
  public ResponseEntity<?> create(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody AppointmentCreate.Request request
  ) {
    String username = userDetails.getUsername();
    AppointmentDto appointmentDto = appointmentService.create(request, username);
    return ResponseEntity.ok(AppointmentCreate.Response.fromDto(appointmentDto));
  }

  @PatchMapping("/patient/my-appointment/{appointmentId}")
  public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable Long appointmentId,
                                  @RequestBody AppointmentUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    AppointmentDto appointmentDto = appointmentService.update(request, appointmentId, username);
    return ResponseEntity.ok(AppointmentUpdate.Response.fromDto(appointmentDto));
  }

  @DeleteMapping("/patient/my-appointment/{appointmentId}")
  public ResponseEntity<?> cancel(@AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable Long appointmentId
  ) {
    String username = userDetails.getUsername();
    appointmentService.cancel(username, appointmentId);
    return ResponseEntity.ok("예약 취소가 완료되었습니다.");
  }

}
