package org.zerobase.hospitalappointmentproject.domain.hospital.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_IS_ALREADY_REGISTERED;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.THIS_HOSPITAL_NAME_ALREADY_EXISTS;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalRegister;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.mapper.HospitalMapper;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class HospitalService {

  private final HospitalRepository hospitalRepository;
  private final StaffRepository staffRepository;
  private final HospitalMapper hospitalMapper;

  /**
   * 병원 관계자의 병원 등록
   *    1. 병원 관계자가 이미 자신의 병원을 등록했는지 확인
   *    2. 동일한 병원 이름이 존재하는지 확인
   *    3. 병원 관계자 엔티티에 병원 저장
   *    4. 병원 등록 후 dto 반환
   */
  public HospitalDto register(String username, HospitalRegister.Request request) {

    StaffEntity staff = staffRepository.findByUsername(username);

    if (staff.getHospital() != null) {
      throw new CustomException(HOSPITAL_IS_ALREADY_REGISTERED);
    }

    if (hospitalRepository.existsByName(request.getName())) {
      throw new CustomException(THIS_HOSPITAL_NAME_ALREADY_EXISTS);
    }

    HospitalEntity hospital = hospitalRepository.save(HospitalRegister.Request.toEntity(request));

    staff = staff.toBuilder().hospital(hospital).build();
    staffRepository.save(staff);

    return hospitalMapper.toDto(hospital);

  }

}
