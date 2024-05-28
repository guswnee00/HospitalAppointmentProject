package org.zerobase.hospitalappointmentproject.domain.hospital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalRegister;
import org.zerobase.hospitalappointmentproject.domain.hospital.service.HospitalService;

@RestController
@RequiredArgsConstructor
public class HospitalController {

  private final HospitalService hospitalService;

  @PostMapping("/staff/my-hospital")
  public ResponseEntity<?> register(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestBody HospitalRegister.Request request
  ) {
    String username = userDetails.getUsername();
    HospitalDto hospitalDto = hospitalService.register(username, request);
    return ResponseEntity.ok(HospitalRegister.Response.fromDto(hospitalDto));
  }

  @PatchMapping("/staff/my-hospital")
  public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody HospitalInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    HospitalDto hospitalDto = hospitalService.updateInfo(username, request);
    return ResponseEntity.ok(HospitalInfoUpdate.Response.fromDto(hospitalDto));
  }

}
