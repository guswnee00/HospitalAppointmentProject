package org.zerobase.hospitalappointmentproject.domain.staff.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.INVALID_PASSWORD;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.TWO_PASSWORDS_DO_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.USERNAME_ALREADY_IN_USE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffDto;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffSignup;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
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
  private final UserValidationService userValidationService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * 병원 관계자의 회원가입
   *    1. 같은 아이디가 있다면 예외 발생
   *    2. 비밀번호 확인
   *        a. 비밀번호와 확인용 비밀번호 일치 확인
   *        b. password util 을 통해 유효한 비밀번호인지 확인
   *    3. 비밀번호 encode
   *    4. 병원 관계자 정보 저장
   *    5. dto 반환
   */
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

  // TODO
  //  - 개인 정보 조회, 수정, 삭제

  /**
   * 병원 관계자의 개인 정보 조회
   *    1. 아이디로 엔티티 가져오기
   *        A. 병원을 등록했다면 가져온 엔티티 그대로 사용
   *        B. 병원을 등록하지 않았다면(staff.getHospital() == null)
   *            a. 병원 이름을 null 로 하는 병원 엔티티 생성
   *            b. staff 엔티티에 a에서 만든 병원 엔티티 삽입
   *    2. mapper 를 이용해 dto 반환
   */
  public StaffDto getInfo(String username) {

    StaffEntity staff = staffRepository.findByUsername(username);

    if (staff.getHospital() == null) {
      HospitalEntity tmpHospital = HospitalEntity.builder().name("null").build();

      StaffEntity tempStaff = StaffEntity.builder()
                                        .username(staff.getUsername())
                                        .name(staff.getName())
                                        .phoneNumber(staff.getPhoneNumber())
                                        .email(staff.getEmail())
                                        .hospital(tmpHospital)
                                        .build();

      return staffMapper.toDto(tempStaff);
    }

    return staffMapper.toDto(staff);

  }

  /*
    병원 관계자의 개인 정보 수정

    public StaffDto updateInfo() {

      }

   */


  /*
    병원 관계자의 개인 정보 삭제

    public StaffDto deleteInfo() {

      }

   */


  // TODO
  //  - 병원 관계자의 병원 등록, 수정, 삭제



}
