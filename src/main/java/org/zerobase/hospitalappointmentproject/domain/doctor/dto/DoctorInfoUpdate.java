package org.zerobase.hospitalappointmentproject.domain.doctor.dto;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;

public class DoctorInfoUpdate {

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
    private String bio;

    public DoctorEntity toUpdateEntity(DoctorEntity entity) {

      DoctorEntity.DoctorEntityBuilder<?, ?> builder = entity.toBuilder();

      Optional.ofNullable(this.name).ifPresent(builder::name);
      Optional.ofNullable(this.phoneNumber).ifPresent(builder::phoneNumber);
      Optional.ofNullable(this.email).ifPresent(builder::email);
      Optional.ofNullable(this.bio).ifPresent(builder::bio);

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

    private String hospital;
    private String specialty;
    private String bio;

    private LocalDateTime modifiedAt;

    public static Response fromDto(DoctorDto dto) {

      return Response.builder()
          .name(dto.getName())
          .phoneNumber(dto.getPhoneNumber())
          .email(dto.getEmail())
          .hospital(dto.getHospital().getName())
          .specialty(dto.getSpecialty().getName())
          .bio(dto.getBio())
          .modifiedAt(dto.getModifiedAt())
          .build();

    }

  }

}
