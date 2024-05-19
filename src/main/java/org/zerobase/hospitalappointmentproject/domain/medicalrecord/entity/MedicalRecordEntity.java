package org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "MedicalRecord")
public class MedicalRecordEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate consultationDate;

  private String diagnosis;

  @Column(columnDefinition = "TEXT")
  private String treatment;

  @Column(columnDefinition = "TEXT")
  private String prescription;

  @ManyToOne
  @JoinColumn(name = "patient_id")
  private PatientEntity patient;

  @ManyToOne
  @JoinColumn(name = "doctor_id")
  private DoctorEntity doctor;

}
