package org.zerobase.hospitalappointmentproject.domain.doctor.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.INVALID_PASSWORD;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.TWO_PASSWORDS_DO_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.USERNAME_ALREADY_IN_USE;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorSignup;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.mapper.DoctorMapper;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.specialty.repository.SpecialtyRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@Service
@RequiredArgsConstructor
public class DoctorService {

  private final DoctorRepository doctorRepository;
  private final DoctorMapper doctorMapper;
  private final SpecialtyRepository specialtyRepository;
  private final HospitalRepository hospitalRepository;
  private final UserValidationService userValidationService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * 의사의 회원가입
   *    1. 같은 아이디가 있다면 예외 발생
   *    2. 비밀번호 확인
   *        a. 비밀번호와 확인용 비밀번호 일치 확인
   *        b. password util 을 통해 유효한 비밀번호인지 확인
   *    3. 비밀번호 encode
   *    4. 의사 정보 저장
   *        a. 진료과목 repo 와 병원 repo 에 존재하는지 확인
   *    5. dto 반환
   */
  public DoctorDto signup(DoctorSignup.Request request) {

    if (userValidationService.isUsernameUsed(request.getUsername())) {
      throw new CustomException(USERNAME_ALREADY_IN_USE);
    }

    if (!PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())) {
      throw new CustomException(TWO_PASSWORDS_DO_NOT_MATCH);
    }

    if (!PasswordUtils.validationPassword(request.getPassword())) {
      throw new CustomException(INVALID_PASSWORD);
    }

    request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

    DoctorEntity doctor = doctorRepository.save(DoctorSignup.Request.toEntity(request, specialtyRepository, hospitalRepository));

    return doctorMapper.toDto(doctor);

  }

  // TODO
  //  - 개인 정보 조회, 수정, 삭제

  /**
   * 의사의 개인 정보 조회
   *    1. 아이디로 엔티티 가져오기
   *    2. mapper 를 이용해 dto 반환
   */
  public DoctorDto getInfo(String username) {

    DoctorEntity doctor = doctorRepository.findByUsername(username);

    return doctorMapper.toDto(doctor);

  }

  /**
   * 의사의 개인 정보 수정
   */

  /**
   * 의사의 개인 정보 삭제
   */

}
