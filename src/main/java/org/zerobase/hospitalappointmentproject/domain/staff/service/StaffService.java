package org.zerobase.hospitalappointmentproject.domain.staff.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.INVALID_PASSWORD;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_IS_REQUIRED_TO_UPDATE_INFO;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.TWO_PASSWORDS_DO_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.USERNAME_ALREADY_IN_USE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffDto;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffSignup;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.mapper.CustomStaffMapper;
import org.zerobase.hospitalappointmentproject.domain.staff.mapper.StaffMapper;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService {

  private final StaffRepository staffRepository;
  private final StaffMapper staffMapper;
  private final CustomStaffMapper customStaffMapper;
  private final UserValidationService userValidationService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * 병원 관계자의 회원가입
   *    1. 같은 아이디가 있다면 예외 발생
   *    2. 비밀번호 확인
   *        A. 비밀번호와 확인용 비밀번호 일치 확인
   *        B. password util 을 통해 유효한 비밀번호인지 확인
   *    3. 비밀번호 encode
   *    4. 병원 관계자 정보 저장
   *    5. dto 반환
   */
  @Transactional
  public StaffDto signup(StaffSignup.Request request) {

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

    StaffEntity staff = staffRepository.save(StaffSignup.Request.toEntity(request));

    return staffMapper.toDto(staff);

  }

  /**
   * 병원 관계자의 개인 정보 조회
   *    1. 아이디로 엔티티 가져오기
   *    2. mapper 를 이용해 dto 반환
   */
  @Transactional
  public StaffDto getInfo(String username) {

    StaffEntity staff = staffRepository.findByUsername(username);

    return customStaffMapper.toDto(staff);

  }

  /*
   * 병원 관계자의 개인 정보 수정
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
  public StaffDto updateInfo(String username, StaffInfoUpdate.Request request) {

    StaffEntity staff = staffRepository.findByUsername(username);

    if (request.getCurrentPassword() != null) {

      if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), staff.getPassword())) {
        throw new CustomException(CURRENT_PASSWORD_DOES_NOT_MATCH);
      } else {
        throw new CustomException(PASSWORD_IS_REQUIRED_TO_UPDATE_INFO);
      }
    }

    if (request.getNewPassword() != null) {

      if (bCryptPasswordEncoder.matches(request.getNewPassword(), staff.getPassword())) {
        throw new CustomException(NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE);
      }

      if (!PasswordUtils.validationPassword(request.getNewPassword())) {
        throw new CustomException(INVALID_PASSWORD);
      }

      staff = staff.toBuilder()
                  .password(bCryptPasswordEncoder.encode(request.getNewPassword()))
                  .build();

    }

    StaffEntity updateEntity = staffRepository.save(request.toUpdateEntity(staff));

    return staffMapper.toDto(updateEntity);

  }

  // TODO
  //  - 병원 관계자의 병원 등록, 수정, 삭제

  /**
   * 병원 관계자의 병원 등록
   *    1. 아이디로 엔티티 가져오기
   *        A. staff.getHospital() != null (이미 병원 등록)
   *            a. CustomException 발생 -> 이미 병원이 등록된 상태입니다.
   *        B. 병원 등록이 안되어 있다면 병원 등록 진행
   *            a. 동일한 병원 이름이 있다면
   *                i. CustomException 발생 -> 동일한 병원 이름이 존재합니다.
   *            b. 동일한 병원 이름이 없다면 등록
   *    2. dto 반환
   *
   *    public HospitalDto registerHospital(String username, RegisterHospital.Request request) {
   *
   *     StaffEntity staff = staffRepository.findByUsername(username);
   *     if (staff.getHospital() != null) {
   *       throw new
   *     }
   *
   *   }
   *
   */




}
