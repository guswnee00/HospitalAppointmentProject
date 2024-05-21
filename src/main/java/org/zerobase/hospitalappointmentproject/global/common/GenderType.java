package org.zerobase.hospitalappointmentproject.global.common;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.INCORRECT_GENDER_CODE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Getter
@RequiredArgsConstructor
public enum GenderType {

  FEMALE("F"),
  NON_BINARY("N"),
  MALE("M");

  private final String initial;

  public static GenderType fromInitial(String initial) {
    for (GenderType gender: GenderType.values()) {
      if (gender.getInitial().equalsIgnoreCase(initial)) {
        return gender;
      }
    }
    throw new CustomException(INCORRECT_GENDER_CODE);
  }

}
