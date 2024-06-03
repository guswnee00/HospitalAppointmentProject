package org.zerobase.hospitalappointmentproject.domain.hospital.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalDto {

  private String name;
  private String address;
  private Double latitude;
  private Double longitude;
  private String contactNumber;
  private String description;

  private LocalTime openTime;
  private LocalTime closeTime;
  private LocalTime lunchStartTime;
  private LocalTime lunchEndTime;

  public static HospitalDto toDto(HospitalEntity entity) {

    return HospitalDto.builder()
        .name(entity.getName())
        .address(entity.getAddress())
        .latitude(entity.getLatitude())
        .longitude(entity.getLongitude())
        .contactNumber(entity.getContactNumber())
        .description(entity.getDescription())
        .openTime(entity.getOpenTime())
        .closeTime(entity.getCloseTime())
        .lunchStartTime(entity.getLunchStartTime())
        .lunchEndTime(entity.getLunchEndTime())
        .build();

  }

}
