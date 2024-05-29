package org.zerobase.hospitalappointmentproject.domain.hospital.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalInfoResponse {

  private String name;
  private String address;
  private String contactNumber;
  private String description;

  private LocalTime openTime;
  private LocalTime closeTime;
  private LocalTime lunchStartTime;
  private LocalTime lunchEndTime;

  public static HospitalInfoResponse fromDto(HospitalDto dto) {

    return HospitalInfoResponse.builder()
        .name(dto.getName())
        .address(dto.getAddress())
        .contactNumber(dto.getContactNumber())
        .description(dto.getDescription())
        .openTime(dto.getOpenTime())
        .closeTime(dto.getCloseTime())
        .lunchStartTime(dto.getLunchStartTime())
        .lunchEndTime(dto.getLunchEndTime())
        .build();

  }

}
