package org.zerobase.hospitalappointmentproject.domain.patient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientDto;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientSignup;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.mapper.PatientMapper;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.exception.ErrorCode;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
  private final PatientMapper patientMapper;
  private final UserValidationService userValidationService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * 환자의 회원가입
   *    1. 같은 아이디가 있다면 예외 발생
   *    2. 비밀번호 확인
   *        a. 비밀번호와 확인용 비밀번호 일치 확인
   *        b. password util 을 통해 유효한 비밀번호인지 확인
   *    3. 비밀번호 encode
   *    4. 환자 정보 저장
   */

  public PatientDto signup(PatientSignup.Request request) {

    if (userValidationService.isUsernameUsed(request.getUsername())) {
      throw new CustomException(ErrorCode.USERNAME_ALREADY_IN_USE);
    }

    if (!PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())) {
      throw new CustomException(ErrorCode.TWO_PASSWORDS_DO_NOT_MATCH);
    }

    if(!PasswordUtils.validationPassword(request.getPassword())) {
      throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }

    request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

    PatientEntity patient = patientRepository.save(PatientSignup.Request.toEntity(request));

    return patientMapper.toDto(patient);
  }

  /**
   * 환자의 개인 정보 조회
   */

  /**
   * 환자의 개인 정보 수정
   */

  /**
   * 환자의 개인 정보 삭제
   */

}
