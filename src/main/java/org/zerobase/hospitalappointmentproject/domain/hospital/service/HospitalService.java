package org.zerobase.hospitalappointmentproject.domain.hospital.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_IS_ALREADY_REGISTERED;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_IS_REQUIRED_TO_UPDATE_INFO;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.THIS_HOSPITAL_NAME_ALREADY_EXISTS;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoUpdate;
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
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * 병원 관계자의 병원 등록
   *    1. 병원 관계자가 이미 자신의 병원을 등록했는지 확인
   *    2. 동일한 병원 이름이 존재하는지 확인
   *    3. 병원 관계자 엔티티에 병원 저장
   *    4. 병원 등록 후 dto 반환
   */
  @Transactional
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


  /*
   * 병원 관계자의 병원 정보 수정
   *    1. 비밀번호 입력 확인(정보를 변경하기 위해서는 비밀번호 입력해야함)
   *    2. 병원 관계자 엔티티 가져와서 비밀번호 확인
   *    3. 해당 스태프의 엔티티에 연결된 병원 엔티티 가져오기
   *    4. 정보 업데이트 후 dto 반환
   */
  @Transactional
  public HospitalDto updateInfo(String username, HospitalInfoUpdate.Request request) {

    if (request.getPassword() == null) {
      throw new CustomException(PASSWORD_IS_REQUIRED_TO_UPDATE_INFO);
    }

    StaffEntity staff = staffRepository.findByUsername(username);

    if (!bCryptPasswordEncoder.matches(request.getPassword(), staff.getPassword())) {
      throw new CustomException(CURRENT_PASSWORD_DOES_NOT_MATCH);
    }

    HospitalEntity hospital = staffRepository.findByUsername(username).getHospital();
    HospitalEntity updateEntity = hospitalRepository.save(request.toUpdateEntity(hospital));

    return hospitalMapper.toDto(updateEntity);

  }

}
