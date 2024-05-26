package org.zerobase.hospitalappointmentproject.domain.patient.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.global.common.GenderType;

public class PatientInfoUpdate {

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

    private String gender;
    private String address;

    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;

    public PatientEntity toUpdateEntity(PatientEntity entity) {

      PatientEntity.PatientEntityBuilder<?, ?> builder = entity.toBuilder();

      if (this.name != null) {
        builder.name(this.name);
      }

      if (this.phoneNumber != null) {
        builder.phoneNumber(this.phoneNumber);
      }

      if (this.email != null) {
        builder.email(this.email);
      }

      if (this.gender != null) {
        builder.gender(GenderType.fromInitial(this.gender));
      }

      if (this.address != null) {
        builder.address(this.address);
      }

      LocalDate currentBirthDay = entity.getBirthDate();
      int year = (this.birthYear != null) ? this.birthYear : currentBirthDay.getYear();
      int month = (this.birthMonth != null) ? this.birthMonth : currentBirthDay.getMonthValue();
      int day = (this.birthDay != null) ? this.birthDay : currentBirthDay.getDayOfMonth();

      builder.birthDate(LocalDate.of(year, month, day));

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

    private GenderType gender;
    private LocalDate birthDate;
    private String address;

    private LocalDateTime modifiedAt;

    public static Response fromDto(PatientDto dto) {

      return Response.builder()
          .name(dto.getName())
          .phoneNumber(dto.getPhoneNumber())
          .email(dto.getEmail())
          .gender(dto.getGender())
          .birthDate(dto.getBirthDate())
          .address(dto.getAddress())
          .modifiedAt(dto.getModifiedAt())
          .build();

    }

  }

}
