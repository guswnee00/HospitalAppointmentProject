package org.zerobase.hospitalappointmentproject.domain.staff.dto;

import java.time.LocalDateTime;
import java.util.Optional;
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

      Optional.ofNullable(this.name).ifPresent(builder::name);
      Optional.ofNullable(this.phoneNumber).ifPresent(builder::phoneNumber);
      Optional.ofNullable(this.email).ifPresent(builder::email);

      return builder.build();

    }

  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private String username;

    private String name;
    private String phoneNumber;
    private String email;

    private String hospitalName;

    private LocalDateTime modifiedAt;

    public static Response fromDto(StaffDto dto) {

      return Response.builder()
          .username(dto.getUsername())
          .name(dto.getName())
          .phoneNumber(dto.getPhoneNumber())
          .email(dto.getEmail())
          .hospitalName(dto.getHospitalName())
          .modifiedAt(dto.getModifiedAt())
          .build();

    }

  }

}
