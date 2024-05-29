package org.zerobase.hospitalappointmentproject.domain.doctor.dto;

import java.time.LocalDateTime;
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

      if (this.name != null) {
        builder.name(this.name);
      }

      if (this.phoneNumber != null) {
        builder.phoneNumber(this.phoneNumber);
      }

      if (this.email != null) {
        builder.email(this.email);
      }

      if (this.bio != null) {
        builder.bio(this.bio);
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
