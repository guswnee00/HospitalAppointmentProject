package org.zerobase.hospitalappointmentproject.domain.hospital.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;

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

    public HospitalEntity toUpdateEntity(HospitalEntity entity) {

      HospitalEntity.HospitalEntityBuilder<?, ?> builder = entity.toBuilder();

      if (this.contactNumber != null) {
        builder.contactNumber(this.contactNumber);
      }

      if (this.description != null) {
        builder.description(this.description);
      }

      if (this.openTime != null) {
        builder.openTime(this.openTime);
      }

      if (this.closeTime != null) {
        builder.closeTime(this.closeTime);
      }

      if (this.lunchStartTime != null) {
        builder.lunchStartTime(this.lunchStartTime);
      }

      if (this.lunchEndTime != null) {
        builder.lunchEndTime(this.lunchEndTime);
      }

      return builder.build();

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
