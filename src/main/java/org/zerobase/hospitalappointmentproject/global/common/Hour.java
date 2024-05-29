package org.zerobase.hospitalappointmentproject.global.common;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.INVALID_TIME;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Getter
@RequiredArgsConstructor
public enum Hour {

  H09(9),
  H10(10),
  H11(11),
  H12(12),
  H13(13),
  H14(14),
  H15(15),
  H16(16),
  H17(17);

  private final int hour;

  public static Hour fromHour(int hour) {

    for (Hour h: values()) {
      if (h.hour == hour) {
        return h;
      }
    }

    throw new CustomException(INVALID_TIME);
  }

}
