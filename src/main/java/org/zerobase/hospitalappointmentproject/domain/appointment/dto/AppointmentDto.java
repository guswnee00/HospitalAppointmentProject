package org.zerobase.hospitalappointmentproject.domain.appointment.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {

  private Long id;

  private String patientName;
  private String specialtyName;
  private String doctorName;
  private String hospitalName;

  private LocalDate appointmentDate;
  private LocalTime appointmentTime;

  private AppointmentStatus status;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static AppointmentDto toDto(AppointmentEntity entity) {

    return AppointmentDto.builder()
        .id(entity.getId())
        .patientName(entity.getPatient().getName())
        .specialtyName(entity.getDoctor().getSpecialty().getName())
        .doctorName(entity.getDoctor().getName())
        .hospitalName(entity.getHospital().getName())
        .appointmentDate(entity.getAppointmentDate())
        .appointmentTime(entity.getAppointmentTime())
        .status(entity.getStatus())
        .createdAt(entity.getCreatedAt())
        .modifiedAt(entity.getModifiedAt())
        .build();

  }

}
