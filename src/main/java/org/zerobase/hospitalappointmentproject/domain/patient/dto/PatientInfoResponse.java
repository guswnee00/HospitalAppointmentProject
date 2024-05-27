package org.zerobase.hospitalappointmentproject.domain.patient.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.global.common.GenderType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientInfoResponse {

  private String username;

  private String name;
  private String phoneNumber;
  private String email;

  private GenderType gender;
  private LocalDate birthDate;

  private String address;

  public static PatientInfoResponse fromDto(PatientDto dto) {

    return PatientInfoResponse.builder()
        .username(dto.getUsername())
        .name(dto.getName())
        .phoneNumber(dto.getPhoneNumber())
        .email(dto.getEmail())
        .gender(dto.getGender())
        .birthDate(dto.getBirthDate())
        .address(dto.getAddress())
        .build();

  }
}
