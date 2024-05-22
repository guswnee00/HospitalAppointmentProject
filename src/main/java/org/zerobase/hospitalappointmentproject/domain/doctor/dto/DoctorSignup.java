package org.zerobase.hospitalappointmentproject.domain.doctor.dto;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_NOT_FOUND;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.specialty.entity.SpecialtyEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.specialty.repository.SpecialtyRepository;
import org.zerobase.hospitalappointmentproject.global.common.PersonRole;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

public class DoctorSignup {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Request {

    private String username;
    private String password;
    private String checkingPassword;

    private String name;
    private String phoneNumber;
    private String email;

    private String bio;
    private String specialty;
    private String hospital;

    public static DoctorEntity toEntity(Request request, SpecialtyRepository specialtyRepository, HospitalRepository hospitalRepository) {

      // 해당 진료 과목이 존재하는지
      SpecialtyEntity specialty = specialtyRepository.findByName(request.getSpecialty());
      if (specialty == null) {
        specialty = SpecialtyEntity.builder().name(request.getSpecialty()).build();
        specialty = specialtyRepository.save(specialty);
      }

      // 해당 병원이 존재하는지
      HospitalEntity hospital = hospitalRepository.findByName(request.getHospital());
      if (hospital == null) {
        throw new CustomException(HOSPITAL_NOT_FOUND);
      }

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
    private String bio;

    private LocalDateTime createdAt;

    public static Response fromDto(DoctorDto dto) {

      return Response.builder()
          .username(dto.getUsername())
          .name(dto.getName())
          .phoneNumber(dto.getPhoneNumber())
          .email(dto.getEmail())
          .hospital(dto.getHospital().getName())
          .bio(dto.getBio())
          .createdAt(dto.getCreatedAt())
          .build();

    }

  }

}
