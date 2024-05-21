package org.zerobase.hospitalappointmentproject.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

  private int statusCode;
  private ErrorCode errorCode;
  private String errorMessage;

  public ErrorResponse(ErrorCode errorCode) {

    this.statusCode = errorCode.getStatusCode();
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getDescription();

  }

}
