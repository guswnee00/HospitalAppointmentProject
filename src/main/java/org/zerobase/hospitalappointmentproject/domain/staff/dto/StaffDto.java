package org.zerobase.hospitalappointmentproject.domain.staff.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;

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

  private HospitalDto hospital;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

}
