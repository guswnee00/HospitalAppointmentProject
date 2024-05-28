package org.zerobase.hospitalappointmentproject.domain.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Hospital")
public class HospitalEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String address;
  private Double latitude;
  private Double longitude;
  private String contactNumber;

  private LocalTime openTime;
  private LocalTime closeTime;
  private LocalTime lunchStartTime;
  private LocalTime lunchEndTime;

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "hospital")
  private Set<DoctorEntity> doctors;

  @OneToMany(mappedBy = "hospital")
  private Set<AppointmentEntity> appointments;

}
