package org.zerobase.hospitalappointmentproject.domain.patient.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.INVALID_PASSWORD;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_IS_REQUIRED_TO_UPDATE_INFO;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PATIENT_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.TWO_PASSWORDS_DO_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.USERNAME_ALREADY_IN_USE;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientDto;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientSignup;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
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
   *    5. dto 반환
   */

  public PatientDto signup(PatientSignup.Request request) {

    if (userValidationService.isUsernameUsed(request.getUsername())) {
      throw new CustomException(USERNAME_ALREADY_IN_USE);
    }

    if (!PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())) {
      throw new CustomException(TWO_PASSWORDS_DO_NOT_MATCH);
    }

    if(!PasswordUtils.validationPassword(request.getPassword())) {
      throw new CustomException(INVALID_PASSWORD);
    }

    request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

    PatientEntity patient = patientRepository.save(PatientSignup.Request.toEntity(request));

    return PatientDto.toDto(patient);

  }

  /**
   * 환자의 개인 정보 조회
   *    1. 아이디로 엔티티 가져오기
   *    2. mapper 를 이용해 dto 반환
   */
  @Transactional
  public PatientDto getInfo(String username) {

    PatientEntity patient = patientRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));

    return PatientDto.toDto(patient);

  }

  /**
   * 환자의 개인 정보 수정
   *    1. 아이디로 엔티티 가져오기
   *    2. 비밀번호 입력 확인(정보를 변경하기 위해서는 비밀번호 입력해야함)
   *        A. 현재 비밀번호와 일치하는지
   *    3. 새로운 비밀번호 유효성 확인
   *        A. 새로운 비밀번호가 현재 비밀번호와 일치하지 않는지
   *        B. 새로운 비밀번호가 유효한지
   *    4. 새로운 비밀번호 encode 하고 request 내용들과 함께 저장
   *    5. dto 반환
   */
  @Transactional
  public PatientDto updateInfo(String username, PatientInfoUpdate.Request request) {

    if (request.getCurrentPassword() == null) {
      throw new CustomException(PASSWORD_IS_REQUIRED_TO_UPDATE_INFO);
    }

    PatientEntity patient = patientRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));

    if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), patient.getPassword())) {
      throw new CustomException(CURRENT_PASSWORD_DOES_NOT_MATCH);
    }

    if (request.getNewPassword() != null) {

      if (bCryptPasswordEncoder.matches(request.getNewPassword(), patient.getPassword())) {
        throw new CustomException(NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE);
      }

      if (!PasswordUtils.validationPassword(request.getNewPassword())) {
        throw new CustomException(INVALID_PASSWORD);
      }

      patient = patient.toBuilder()
          .password(bCryptPasswordEncoder.encode(request.getNewPassword()))
          .build();

    }

    PatientEntity updateEntity = patientRepository.save(request.toUpdateEntity(patient));

    return PatientDto.toDto(updateEntity);

  }

  /**
   * 환자의 개인 정보 삭제
   *    1. 아이디로 엔티티 가져오기
   *    2. 비밀번호를 입력했는지 일치하는지 확인
   *    3. 엔티티 삭제
   */
  @Transactional
  public void deleteInfo(String username, String password) {

    PatientEntity patient = patientRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));

    if (password == null || !bCryptPasswordEncoder.matches(password, patient.getPassword())) {
      throw new CustomException(PASSWORD_DOES_NOT_MATCH);
    }

    patientRepository.delete(patient);

  }

}
