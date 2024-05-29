package org.zerobase.hospitalappointmentproject.global.common;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.INVALID_TIME;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Getter
@RequiredArgsConstructor
public enum Minute {

  M00(0),
  M15(15),
  M30(30),
  M45(45);

  private final int minute;

  public static Minute fromMinute(int minute) {

    for (Minute m: values()) {
      if (m.minute == minute) {
        return m;
      }
    }

    throw new CustomException(INVALID_TIME);

  }

}
