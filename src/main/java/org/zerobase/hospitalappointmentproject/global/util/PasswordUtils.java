package org.zerobase.hospitalappointmentproject.global.util;

public class PasswordUtils {

  public static boolean equalPassword(String password1, String password2) {
    // 비밀번호와 확인용 비밀번호가 일치하는지
    return password1.equals(password2);
  }
  public static boolean validationPassword(String password) {

    // 비밀번호의 길이가 8 이상인지
    if (password.length() < 8) {
      return false;
    }

    // 동일한 문자가 4번이상 반복되는지
    if (checkRepeat(password)) {
      return false;
    }

    // 영어 대소문자와 숫자로 이루어져있는지
    if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
      return false;
    }

    return true;

  }

  private static boolean checkRepeat(String password) {

    int count = 1;

    for (int i = 1; i < password.length(); i++) {
      if (password.charAt(i) == password.charAt(i - 1)) {
        count++;
        if (count >= 4) {
          return true;
        }
      } else {
        count = 1;
      }
    }

    return false;

  }

}
