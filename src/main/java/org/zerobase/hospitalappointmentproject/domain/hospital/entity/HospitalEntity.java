package org.zerobase.hospitalappointmentproject.domain.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoUpdate;

@SuperBuilder(toBuilder = true)
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

  public HospitalEntity toUpdateEntity(HospitalInfoUpdate.Request request) {

    Optional.ofNullable(request.getContactNumber()).ifPresent(value -> this.contactNumber = value);
    Optional.ofNullable(request.getDescription()).ifPresent(value -> this.description = value);
    Optional.ofNullable(request.getOpenTime()).ifPresent(value -> this.openTime = value);
    Optional.ofNullable(request.getCloseTime()).ifPresent(value -> this.closeTime = value);
    Optional.ofNullable(request.getLunchStartTime()).ifPresent(value -> this.lunchStartTime = value);
    Optional.ofNullable(request.getLunchEndTime()).ifPresent(value -> this.lunchEndTime = value);

    return this;

  }

}
