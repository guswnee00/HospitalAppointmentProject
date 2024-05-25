package org.zerobase.hospitalappointmentproject.domain.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorInfoResponse {

  private String username;

  private String name;
  private String phoneNumber;
  private String email;

  private String specialtyName;
  private String hospitalName;

  public static DoctorInfoResponse fromDto(DoctorDto dto) {

    return DoctorInfoResponse.builder()
        .username(dto.getUsername())
        .name(dto.getName())
        .phoneNumber(dto.getPhoneNumber())
        .email(dto.getEmail())
        .specialtyName(dto.getSpecialty().getName())
        .hospitalName(dto.getHospital().getName())
        .build();

  }

}
