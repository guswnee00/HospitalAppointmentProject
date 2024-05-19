package org.zerobase.hospitalappointmentproject.domain.patient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;
import org.zerobase.hospitalappointmentproject.global.common.BaseEntity;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Patient")
public class PatientEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;      // 로그인 아이디
  private String password;
  private String role;

  private String name;          // 실제 이름
  private String gender;
  private LocalDate birthDate;
  private String phoneNumber;
  private String email;
  private String address;

  @OneToMany(mappedBy = "patient")
  private Set<MedicalRecordEntity> medicalRecords;

  @OneToMany(mappedBy = "patient")
  private Set<AppointmentEntity> appointments;

}
