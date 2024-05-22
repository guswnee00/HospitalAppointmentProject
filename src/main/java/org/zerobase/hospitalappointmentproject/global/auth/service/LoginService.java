package org.zerobase.hospitalappointmentproject.global.auth.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.USERNAME_DOES_NOT_EXIST;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.auth.dto.LoginDto;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

/**
 * 로그인
 *    1. 로그인 할 떄 입력한 아이디로 엔티티 검색
 *    2. 비밀번호 확인
 *    3. 엔티티 반환
 */

@RequiredArgsConstructor
@Service
public class LoginService {

  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final StaffRepository staffRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public PatientEntity patientLogin(LoginDto loginDto) {

    PatientEntity patient = patientRepository.findByUsername(loginDto.getUsername());

    if (patient == null) {
      throw new CustomException(USERNAME_DOES_NOT_EXIST);
    }

    if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), patient.getPassword())) {
      throw new CustomException(PASSWORD_DOES_NOT_MATCH);
    }

    return patient;
  }

  public DoctorEntity doctorLogin(LoginDto loginDto) {

    DoctorEntity doctor = doctorRepository.findByUsername(loginDto.getUsername());

    if (doctor == null) {
      throw new CustomException(USERNAME_DOES_NOT_EXIST);
    }

    if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), doctor.getPassword())) {
      throw new CustomException(PASSWORD_DOES_NOT_MATCH);
    }

    return doctor;

  }

  public StaffEntity staffLogin(LoginDto loginDto) {

    StaffEntity staff = staffRepository.findByUsername(loginDto.getUsername());

    if (staff == null) {
      throw new CustomException(USERNAME_DOES_NOT_EXIST);
    }

    if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), staff.getPassword())) {
      throw new CustomException(PASSWORD_DOES_NOT_MATCH);
    }

    return staff;

  }

}
