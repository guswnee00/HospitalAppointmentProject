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

  //gender type
  INCORRECT_GENDER_CODE(HttpStatus.NOT_FOUND.value(), "올바르지 않은 성별 코드입니다."),

  // hospital
  HOSPITAL_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 병원이 존재하지 않습니다."),



  ;

  private final int statusCode;
  private final String description;
}
