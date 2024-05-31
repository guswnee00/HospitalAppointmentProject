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
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;
import org.zerobase.hospitalappointmentproject.global.common.Hour;
import org.zerobase.hospitalappointmentproject.global.common.Minute;

public class AppointmentCreate {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private String hospitalName;
    private String doctorName;

    private LocalDate appointmentDate;
    private Hour appointmentHour;
    private Minute appointmentMinute;

    public static AppointmentEntity toEntity(Request request, PatientEntity patient, HospitalEntity hospital, DoctorEntity doctor) {

      return AppointmentEntity.builder()
          .patient(patient)
          .hospital(hospital)
          .doctor(doctor)
          .appointmentDate(request.getAppointmentDate())
          .appointmentTime(LocalTime.of(request.getAppointmentHour().getHour(), request.getAppointmentMinute().getMinute()))
          .status(AppointmentStatus.PENDING_APPOINTMENT)
          .build();

    }

  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private String hospitalName;
    private String doctorName;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private AppointmentStatus status;

    private LocalDateTime createdAt;

    public static Response fromDto(AppointmentDto dto) {

      return Response.builder()
          .hospitalName(dto.getHospitalName())
          .doctorName(dto.getDoctorName())
          .appointmentDate(dto.getAppointmentDate())
          .appointmentTime(dto.getAppointmentTime())
          .status(dto.getStatus())
          .createdAt(dto.getCreatedAt())
          .build();

    }

  }

}
