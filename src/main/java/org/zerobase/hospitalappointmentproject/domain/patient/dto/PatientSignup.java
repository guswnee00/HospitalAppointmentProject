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
import org.zerobase.hospitalappointmentproject.global.common.PersonRole;

public class PatientSignup {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private String username;
    private String password;
    private String checkingPassword;

    private String name;
    private String phoneNumber;
    private String email;

    private String gender;
    private String address;

    private int birthYear;
    private int birthMonth;
    private int birthDay;

    public static PatientEntity toEntity(Request request) {

      return PatientEntity.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .role(PersonRole.ROLE_PATIENT.toString())
          .name(request.getName())
          .phoneNumber(request.getPhoneNumber())
          .email(request.getEmail())
          .gender(GenderType.fromInitial(request.getGender()))
          .birthDate(LocalDate.of(request.getBirthYear(),
                                  request.getBirthMonth(),
                                  request.getBirthDay()))
          .address(request.getAddress())
          .build();

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

    private GenderType gender;
    private LocalDate birthDate;
    private String address;

    private LocalDateTime createdAt;

    public static Response fromDto(PatientDto dto) {

      return Response.builder()
          .username(dto.getUsername())
          .name(dto.getName())
          .phoneNumber(dto.getPhoneNumber())
          .email(dto.getEmail())
          .gender(dto.getGender())
          .birthDate(dto.getBirthDate())
          .address(dto.getAddress())
          .createdAt(dto.getCreatedAt())
          .build();

    }

  }

}
