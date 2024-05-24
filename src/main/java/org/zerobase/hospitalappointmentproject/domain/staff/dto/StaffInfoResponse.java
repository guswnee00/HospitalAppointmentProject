package org.zerobase.hospitalappointmentproject.domain.staff.dto;

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
public class StaffInfoResponse {

  private String username;

  private String name;
  private String phoneNumber;
  private String email;

  private String hospitalName;

  public static StaffInfoResponse fromDto(StaffDto dto) {
    return StaffInfoResponse.builder()
        .username(dto.getUsername())
        .name(dto.getName())
        .phoneNumber(dto.getPhoneNumber())
        .email(dto.getEmail())
        .hospitalName(dto.getHospital().getName())
        .build();
  }

}
