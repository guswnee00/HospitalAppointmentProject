package org.zerobase.hospitalappointmentproject.domain.doctor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;
import org.zerobase.hospitalappointmentproject.global.common.BaseEntity;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Doctor")
public class DoctorEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;      // 로그인 아이디
  private String password;
  private String role;

  private String name;          // 실제 이름
  private String phoneNumber;
  private String email;

  @Column(columnDefinition = "TEXT")
  private String bio;

  @OneToOne
  @JoinColumn(name = "specialty_id")
  private SpecialtyEntity specialty;

  @ManyToOne
  @JoinColumn(name = "hospital_id")
  private HospitalEntity hospital;

  @OneToMany(mappedBy = "doctor")
  private Set<MedicalRecordEntity> medicalRecords;

  @OneToMany(mappedBy = "doctor")
  private Set<AppointmentEntity> appointments;

}
