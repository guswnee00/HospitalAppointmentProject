package org.zerobase.hospitalappointmentproject.domain.staff.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.mapper.HospitalMapper;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffDto;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;

@Component
@RequiredArgsConstructor
public class CustomStaffMapper {

  private final HospitalMapper hospitalMapper;

  public StaffDto toDto(StaffEntity entity) {
    if (entity == null) {
      return null;
    }

    HospitalDto hospitalDto;
    if (entity.getHospital() != null) {
      hospitalDto = hospitalMapper.toDto(entity.getHospital());
    } else {
      hospitalDto = HospitalDto.builder()
          .name(null)
          .build();
    }

    return StaffDto.builder()
        .username(entity.getUsername())
        .name(entity.getName())
        .phoneNumber(entity.getPhoneNumber())
        .email(entity.getEmail())
        .hospital(hospitalDto)
        .build();
  }

}
