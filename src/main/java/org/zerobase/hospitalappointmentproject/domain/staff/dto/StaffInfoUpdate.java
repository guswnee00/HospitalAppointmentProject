package org.zerobase.hospitalappointmentproject.domain.staff.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;

public class StaffInfoUpdate {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private String currentPassword;
    private String newPassword;

    private String name;
    private String phoneNumber;
    private String email;

    public StaffEntity toUpdateEntity(StaffEntity entity) {

      StaffEntity.StaffEntityBuilder<?, ?> builder = entity.toBuilder();

      if (this.name != null) {
        builder.name(this.name);
      }

      if (this.phoneNumber != null) {
        builder.phoneNumber(this.phoneNumber);
      }

      if (this.email != null) {
        builder.email(this.email);
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
    private String phoneNumber;
    private String email;

    private LocalDateTime modifiedAt;

    public static Response fromDto(StaffDto dto) {

      return Response.builder()
          .name(dto.getName())
          .phoneNumber(dto.getPhoneNumber())
          .email(dto.getEmail())
          .modifiedAt(dto.getModifiedAt())
          .build();

    }

  }

}
