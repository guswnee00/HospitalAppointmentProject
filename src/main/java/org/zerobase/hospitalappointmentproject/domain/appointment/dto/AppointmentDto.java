package org.zerobase.hospitalappointmentproject.domain.appointment.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {

  private LocalDate appointmentDate;
  private LocalTime appointmentTime;

  private AppointmentStatus status;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  private String patientName;
  private String doctorName;
  private String hospitalName;

}
