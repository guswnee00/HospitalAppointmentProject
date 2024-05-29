package org.zerobase.hospitalappointmentproject.domain.hospital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoResponse;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalRegister;
import org.zerobase.hospitalappointmentproject.domain.hospital.service.HospitalService;
import org.zerobase.hospitalappointmentproject.global.auth.dto.PasswordRequest;

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
  public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody HospitalInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    HospitalDto hospitalDto = hospitalService.updateInfo(username, request);
    return ResponseEntity.ok(HospitalInfoUpdate.Response.fromDto(hospitalDto));
  }

  @DeleteMapping("/staff/my-hospital")
  public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody PasswordRequest request
  ) {
    String username = userDetails.getUsername();
    hospitalService.delete(username, request.getPassword());
    return ResponseEntity.ok("병원 삭제가 완료되었습니다.");
  }

  @GetMapping("/search/{hospitalName}")
  public ResponseEntity<?> getInfo(@PathVariable String hospitalName) {
    HospitalDto hospitalDto = hospitalService.getInfo(hospitalName);
    return ResponseEntity.ok(HospitalInfoResponse.fromDto(hospitalDto));
  }

}
