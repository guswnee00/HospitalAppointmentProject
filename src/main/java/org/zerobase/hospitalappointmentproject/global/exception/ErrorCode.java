package org.zerobase.hospitalappointmentproject.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // signup
  USERNAME_ALREADY_IN_USE(HttpStatus.CONFLICT.value(), "이미 사용중인 아이디 입니다."),
  TWO_PASSWORDS_DO_NOT_MATCH(HttpStatus.BAD_REQUEST.value(), "비밀번호와 확인용 비밀번호가 일치하지 않습니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 비밀번호 입니다."),

  // login
  USERNAME_DOES_NOT_EXIST(HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 아이디 입니다."),

  // update info
  PASSWORD_IS_REQUIRED_TO_UPDATE_INFO(HttpStatus.BAD_REQUEST.value(),"회원정보를 수정하기 위해서는 현재 비밀번호를 입력해야 합니다."),
  CURRENT_PASSWORD_DOES_NOT_MATCH(HttpStatus.UNAUTHORIZED.value(),"현재 비밀번호와 일치하지 않습니다."),
  NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE(HttpStatus.BAD_REQUEST.value(),"이전과 동일한 비밀번호를 사용할 수 없습니다."),

  // delete info
  PASSWORD_DOES_NOT_MATCH(HttpStatus.UNAUTHORIZED.value(),"비밀번호가 일치하지 않습니다."),

  // patient
  PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 환자가 존재하지 않습니다."),

  // doctor
  DOCTOR_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 의사가 존재하지 않습니다."),

  // staff
  STAFF_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 병원 관계자가 존재하지 않습니다."),

  // specialty
  SPECIALTY_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 진료과목이 존재하지 않습니다."),

  // medical record
  MEDICAL_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 진료기록이 존재하지 않습니다."),

  // hospital
  HOSPITAL_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 병원이 존재하지 않습니다."),
  HOSPITAL_IS_ALREADY_REGISTERED(HttpStatus.CONFLICT.value(), "이미 병원을 등록한 상태입니다."),
  THIS_HOSPITAL_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "이미 존재하는 병원 이름입니다."),

  // appointment
  NOT_HOSPITAL_OPERATING_HOUR(HttpStatus.BAD_REQUEST.value(), "병원 운영시간이 아닙니다."),
  BREAK_TIME_FOR_LUNCH(HttpStatus.BAD_REQUEST.value(), "병원 점심 휴게시간 입니다."),
  DOCTOR_IS_NOT_AVAILABLE(HttpStatus.CONFLICT.value(), "해당 시간에는 이미 예약이 차있습니다."),
  APPOINTMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 예약이 존재하지 않습니다."),
  CANNOT_MODIFICATION(HttpStatus.CONFLICT.value(),"해당 예약을 수정할 수 없습니다."),
  CANNOT_CANCEL(HttpStatus.CONFLICT.value(),"해당 예약을 취소할 수 없습니다."),
  TODAY_IS_NOT_A_APPOINTMENT_DAY(HttpStatus.BAD_REQUEST.value(), "오늘은 예약일이 아닙니다."),
  ARRIVAL_CONFIRMATION_TIME_HAS_PASSED(HttpStatus.UNPROCESSABLE_ENTITY.value(), "예약 시간 15분 전에 도착을 완료했어야 합니다."),
  CHECK_YOUR_APPOINTMENT_STATUS(HttpStatus.CONFLICT.value(), "예약 대기상태에서는 도착 확인을 할 수 없습니다. 예약 상태를 확인해주세요."),

  //gender type
  INVALID_GENDER_CODE(HttpStatus.NOT_FOUND.value(), "올바르지 않은 성별 코드입니다."),

  // hour, minute
  INVALID_TIME(HttpStatus.NOT_FOUND.value(), "올바르지 않은 시간 입력입니다."),

  // sms
  FAILED_TO_SEND_MESSAGE(HttpStatus.FAILED_DEPENDENCY.value(), "예약 문자 발송에 실패했습니다."),

  ;

  private final int statusCode;
  private final String description;
}
