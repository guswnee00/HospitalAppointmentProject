package org.zerobase.hospitalappointmentproject.domain.hospital.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
