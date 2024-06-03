package org.zerobase.hospitalappointmentproject.domain.staff.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDto {

  private String username;      // 로그인 아이디
  private String password;
  private String role;          // jwt 역할별 접근 제어

  private String name;          // 실제 이름
  private String phoneNumber;
  private String email;

  private String hospitalName;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static StaffDto toDto(StaffEntity entity) {

    return StaffDto.builder()
        .username(entity.getUsername())
        .password(entity.getPassword())
        .role(entity.getRole())
        .name(entity.getName())
        .phoneNumber(entity.getPhoneNumber())
        .email(entity.getEmail())
        .hospitalName(entity.getHospital() != null ? entity.getHospital().getName() : null)
        .createdAt(entity.getCreatedAt())
        .modifiedAt(entity.getModifiedAt())
        .build();

  }

}
