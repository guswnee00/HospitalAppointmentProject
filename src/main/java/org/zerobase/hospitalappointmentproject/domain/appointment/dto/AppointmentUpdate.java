package org.zerobase.hospitalappointmentproject.domain.appointment.dto;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_NOT_FOUND;

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
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;
import org.zerobase.hospitalappointmentproject.global.common.Hour;
import org.zerobase.hospitalappointmentproject.global.common.Minute;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

public class AppointmentUpdate {

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

    public AppointmentEntity toUpdateEntity(AppointmentEntity entity, HospitalRepository hospitalRepository, DoctorRepository doctorRepository) {

      AppointmentEntity.AppointmentEntityBuilder builder = entity.toBuilder();

      Optional.ofNullable(this.hospitalName).ifPresent(name -> {
        HospitalEntity hospital = hospitalRepository.findByName(name)
            .orElseThrow(() -> new CustomException(HOSPITAL_NOT_FOUND));
        builder.hospital(hospital);
      });

      Optional.ofNullable(this.doctorName).ifPresent(name -> {
        DoctorEntity doctor = doctorRepository.findByName(name)
            .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));
        builder.doctor(doctor);
      });

      Optional.ofNullable(this.appointmentDate).ifPresent(builder::appointmentDate);

      LocalTime beforeChange = entity.getAppointmentTime();
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
    private String doctorName;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private AppointmentStatus status;

    private LocalDateTime modifiedAt;

    public static Response fromDto(AppointmentDto dto) {

      return Response.builder()
          .hospitalName(dto.getHospitalName())
          .doctorName(dto.getDoctorName())
          .appointmentDate(dto.getAppointmentDate())
          .appointmentTime(dto.getAppointmentTime())
          .status(dto.getStatus())
          .modifiedAt(dto.getModifiedAt())
          .build();
    }

  }

}
