package org.zerobase.hospitalappointmentproject.domain.hospital.dto;

import java.time.LocalTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentDto;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalDto {

  private String name;
  private String address;
  private Double latitude;
  private Double longitude;
  private String contactNumber;
  private String description;

  private LocalTime openTime;
  private LocalTime closeTime;
  private LocalTime lunchStartTime;
  private LocalTime lunchEndTime;

  private Set<DoctorDto> doctors;
  private Set<AppointmentDto> appointments;

}
