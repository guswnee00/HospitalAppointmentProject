package org.zerobase.hospitalappointmentproject.domain.doctor.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.specialty.entity.SpecialtyEntity;
import org.zerobase.hospitalappointmentproject.global.common.PersonRole;

public class DoctorSignup {
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

    private String bio;
    private String specialtyName;
    private String hospitalName;

    public static DoctorEntity toEntity(Request request, SpecialtyEntity specialty, HospitalEntity hospital) {

      return DoctorEntity.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .role(PersonRole.ROLE_DOCTOR.toString())
          .name(request.getName())
          .phoneNumber(request.getPhoneNumber())
          .email(request.getEmail())
          .bio(request.getBio())
          .specialty(specialty)
          .hospital(hospital)
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

    private String hospital;
    private String specialty;
    private String bio;

    private LocalDateTime createdAt;

    public static Response fromDto(DoctorDto dto) {

      return Response.builder()
          .username(dto.getUsername())
          .name(dto.getName())
          .phoneNumber(dto.getPhoneNumber())
          .email(dto.getEmail())
          .hospital(dto.getHospitalName())
          .specialty(dto.getSpecialtyName())
          .bio(dto.getBio())
          .createdAt(dto.getCreatedAt())
          .build();

    }

  }

}
