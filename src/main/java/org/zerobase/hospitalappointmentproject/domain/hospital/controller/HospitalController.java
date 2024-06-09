package org.zerobase.hospitalappointmentproject.domain.hospital.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoResponse;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalRegister;
import org.zerobase.hospitalappointmentproject.domain.hospital.service.HospitalService;
import org.zerobase.hospitalappointmentproject.global.auth.dto.PasswordRequest;

@RestController
@RequiredArgsConstructor
@Tag(name = "Hospital API", description = "병원 API 입니다.")
public class HospitalController {

  private final HospitalService hospitalService;

  @Operation(summary = "hospital register", description = "병원 관계자가 병원을 등록 합니다.")
  @Parameter(name = "name", description = "병원 이름")
  @Parameter(name = "address", description = "병원 주소")
  @Parameter(name = "latitude", description = "위도")
  @Parameter(name = "longitude", description = "경도")
  @Parameter(name = "contactNumber", description = "예약 시")
  @Parameter(name = "description", description = "병원 설명")
  @Parameter(name = "openTime", description = "병원 진료 시작 시간")
  @Parameter(name = "closeTime", description = "병원 진료 종료 시간")
  @Parameter(name = "lunchStartTime", description = "점심 휴게 시작 시간")
  @Parameter(name = "lunchEndTime", description = "점심 휴게 종료 시간")
  @PostMapping("/staff/my-hospital")
  public ResponseEntity<?> register(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestBody HospitalRegister.Request request
  ) {
    String username = userDetails.getUsername();
    HospitalDto hospitalDto = hospitalService.register(username, request);
    return ResponseEntity.ok(HospitalRegister.Response.fromDto(hospitalDto));
  }

  @Operation(summary = "hospital information update", description = "병원 관계자가 병원 정보를 수정합니다.")
  @Parameter(name = "password", description = "병원 관계자의 비밀번호")
  @Parameter(name = "contactNumber", description = "예약 시")
  @Parameter(name = "description", description = "병원 설명")
  @Parameter(name = "openTime", description = "병원 진료 시작 시간")
  @Parameter(name = "closeTime", description = "병원 진료 종료 시간")
  @Parameter(name = "lunchStartTime", description = "점심 휴게 시작 시간")
  @Parameter(name = "lunchEndTime", description = "점심 휴게 종료 시간")
  @PatchMapping("/staff/my-hospital")
  public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody HospitalInfoUpdate.Request request
  ) {
    String username = userDetails.getUsername();
    HospitalDto hospitalDto = hospitalService.updateInfo(username, request);
    return ResponseEntity.ok(HospitalInfoUpdate.Response.fromDto(hospitalDto));
  }

  @Operation(summary = "hospital information delete", description = "병원 관계자가 병원 정보를 삭제합니다.")
  @Parameter(name = "password", description = "병원 관계자의 비밀번호")
  @DeleteMapping("/staff/my-hospital")
  public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody PasswordRequest request
  ) {
    String username = userDetails.getUsername();
    hospitalService.delete(username, request.getPassword());
    return ResponseEntity.ok("병원 삭제가 완료되었습니다.");
  }

  @Operation(summary = "list of hospital medical specialty", description = "병원의 진료 과목들을 확인합니다.")
  @GetMapping("/search/{hospitalName}/specialties")
  public ResponseEntity<?> specialtyList(@Parameter(name = "hospitalName", description = "병원 이름")
                                         @PathVariable String hospitalName
  ) {
    List<String> specialties = hospitalService.specialtyList(hospitalName);
    return ResponseEntity.ok(specialties);
  }

  @Operation(summary = "list of hospital with specialty", description = "진료 과목으로 병원을 검색합니다.")
  @GetMapping("/search/{specialtyName}")
  public ResponseEntity<?> searchBySpecialtyName(@Parameter(name = "specialtyName", description = "진료 과목 이름")
                                                 @PathVariable String specialtyName
  ) {
    List<HospitalDto> hospitalDtos = hospitalService.searchBySpecialtyName(specialtyName);
    List<HospitalInfoResponse> hospitalInfoResponses = hospitalDtos.stream().map(HospitalInfoResponse::fromDto).toList();
    return ResponseEntity.ok(hospitalInfoResponses);
  }

  @Operation(summary = "list of hospital with name", description = "병원 이름으로 병원을 검색합니다.")
  @GetMapping("/search/{hospitalName}")
  public ResponseEntity<?> searchByHospitalName(@Parameter(name = "hospitalName", description = "병원 이름")
                                                @PathVariable String hospitalName
  ) {
    HospitalDto hospitalDto = hospitalService.searchByHospitalName(hospitalName);
    return ResponseEntity.ok(HospitalInfoResponse.fromDto(hospitalDto));
  }

  @Operation(summary = "search nearby hospital", description = "근처 반경 n km의 병원을 검색합니다.")
  @GetMapping("/search/nearby")
  public ResponseEntity<?> searchNearBy(@RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
    List<HospitalDto> hospitalDtos = hospitalService.searchNearBy(lat, lon, radius);
    return ResponseEntity.ok(hospitalDtos);
  }

  @GetMapping("/search/hospital")
  public ResponseEntity<?> searchByHospitalNameES(@RequestParam String hospitalName) {
    List<HospitalDocument> hospitalDocuments = hospitalService.searchByHospitalNameES(hospitalName);
    return ResponseEntity.ok(hospitalDocuments);
  }
}
