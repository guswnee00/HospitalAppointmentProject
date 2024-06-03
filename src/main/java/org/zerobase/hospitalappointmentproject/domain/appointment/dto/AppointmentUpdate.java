package org.zerobase.hospitalappointmentproject.domain.appointment.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;
import org.zerobase.hospitalappointmentproject.global.common.Hour;
import org.zerobase.hospitalappointmentproject.global.common.Minute;

public class AppointmentUpdate {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private String hospitalName;
    private String specialtyName;
    private String doctorName;

    private LocalDate appointmentDate;
    private Hour appointmentHour;
    private Minute appointmentMinute;

    public AppointmentEntity toUpdateEntity(AppointmentEntity appointment, HospitalEntity hospital, DoctorEntity doctor) {

      AppointmentEntity.AppointmentEntityBuilder builder = appointment.toBuilder();
      builder.hospital(hospital);
      builder.doctor(doctor);

      Optional.ofNullable(this.appointmentDate).ifPresent(builder::appointmentDate);

      LocalTime beforeChange = appointment.getAppointmentTime();
      int updateHour = Optional.ofNullable(this.appointmentHour).map(Hour::getHour)
                               .orElse(beforeChange.getHour());
      int updateMinute = Optional.ofNullable(this.appointmentMinute).map(Minute::getMinute)
                                 .orElse(beforeChange.getMinute());
      builder.appointmentTime(LocalTime.of(updateHour, updateMinute));

      return builder.build();

    }

  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private String hospitalName;
    private String specialtyName;
    private String doctorName;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private AppointmentStatus status;

    private LocalDateTime modifiedAt;

    public static Response fromDto(AppointmentDto dto) {

      return Response.builder()
          .hospitalName(dto.getHospitalName())
          .specialtyName(dto.getSpecialtyName())
          .doctorName(dto.getDoctorName())
          .appointmentDate(dto.getAppointmentDate())
          .appointmentTime(dto.getAppointmentTime())
          .status(dto.getStatus())
          .modifiedAt(dto.getModifiedAt())
          .build();
    }

  }

}
