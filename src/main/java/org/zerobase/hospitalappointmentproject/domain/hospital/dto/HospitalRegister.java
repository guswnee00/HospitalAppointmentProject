package org.zerobase.hospitalappointmentproject.domain.hospital.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;

public class HospitalRegister {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

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

    public static HospitalEntity toEntity(Request request) {

      return HospitalEntity.builder()
          .name(request.getName())
          .address(request.getAddress())
          .latitude(request.getLatitude())
          .longitude(request.getLongitude())
          .contactNumber(request.getContactNumber())
          .description(request.getDescription())
          .openTime(request.getOpenTime())
          .closeTime(request.getCloseTime())
          .lunchStartTime(request.getLunchStartTime())
          .lunchEndTime(request.getLunchEndTime())
          .build();

    }

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
