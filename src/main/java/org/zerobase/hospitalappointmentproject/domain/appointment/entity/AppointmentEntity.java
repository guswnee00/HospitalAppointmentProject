package org.zerobase.hospitalappointmentproject.domain.appointment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;
import org.zerobase.hospitalappointmentproject.global.common.BaseEntity;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Appointment")
public class AppointmentEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime appointmentTime;

  @Enumerated(EnumType.STRING)
  private AppointmentStatus status;

  @ManyToOne
  @JoinColumn(name = "patient_id")
  private PatientEntity patient;

  @ManyToOne
  @JoinColumn(name = "doctor_id")
  private DoctorEntity doctor;

  @ManyToOne
  @JoinColumn(name = "hospital_id")
  private HospitalEntity hospital;

}
