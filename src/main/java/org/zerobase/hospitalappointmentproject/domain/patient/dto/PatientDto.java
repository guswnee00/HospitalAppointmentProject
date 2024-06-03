package org.zerobase.hospitalappointmentproject.domain.patient.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.global.common.GenderType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {

  private String username;      // 로그인 아이디
  private String password;
  private String role;          // jwt 역할별 접근 제어

  private String name;          // 실제 이름
  private String phoneNumber;
  private String email;
  private GenderType gender;
  private LocalDate birthDate;
  private String address;

  private Set<Long> medicalRecords;
  private Set<Long> appointments;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static PatientDto toDto(PatientEntity entity) {

    return PatientDto.builder()
        .username(entity.getUsername())
        .password(entity.getPassword())
        .role(entity.getRole())
        .name(entity.getName())
        .phoneNumber(entity.getPhoneNumber())
        .email(entity.getEmail())
        .gender(entity.getGender())
        .birthDate(entity.getBirthDate())
        .address(entity.getAddress())
        .medicalRecords(Optional.ofNullable(entity.getMedicalRecords())
                                .orElse(Collections.emptySet())
                                .stream()
                                .map(MedicalRecordEntity::getId)
                                .collect(Collectors.toSet()))
        .appointments(Optional.ofNullable(entity.getAppointments())
                              .orElse(Collections.emptySet())
                              .stream()
                              .map(AppointmentEntity::getId)
                              .collect(Collectors.toSet()))
        .createdAt(entity.getCreatedAt())
        .modifiedAt(entity.getModifiedAt())
        .build();

  }

}
