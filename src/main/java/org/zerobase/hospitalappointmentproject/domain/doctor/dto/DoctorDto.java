package org.zerobase.hospitalappointmentproject.domain.doctor.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordDto;
import org.zerobase.hospitalappointmentproject.domain.specialty.dto.SpecialtyDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDto {

  private String username;      // 로그인 아이디
  private String password;
  private String role;          // jwt 역할별 접근 제어

  private String name;          // 실제 이름
  private String phoneNumber;
  private String email;

  private String bio;

  private SpecialtyDto specialty;
  private HospitalDto hospital;
  private Set<MedicalRecordDto> medicalRecords;
  private Set<AppointmentDto> appointments;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

}
