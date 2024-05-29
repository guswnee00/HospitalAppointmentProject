package org.zerobase.hospitalappointmentproject.domain.hospital.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class HospitalInfoUpdate {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private String password;

    private String contactNumber;
    private String description;

    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;

  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

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

    public static Response fromDto(HospitalDto dto) {

      return Response.builder()
          .name(dto.getName())
          .address(dto.getAddress())
          .latitude(dto.getLatitude())
          .longitude(dto.getLongitude())
          .contactNumber(dto.getContactNumber())
          .description(dto.getDescription())
          .openTime(dto.getOpenTime())
          .closeTime(dto.getCloseTime())
          .lunchStartTime(dto.getLunchStartTime())
          .lunchEndTime(dto.getLunchEndTime())
          .build();

    }

  }

}
