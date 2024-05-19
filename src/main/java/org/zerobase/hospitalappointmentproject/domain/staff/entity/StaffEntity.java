package org.zerobase.hospitalappointmentproject.domain.staff.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.global.common.BaseEntity;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Staff")
@Table(name = "STAFF")
public class StaffEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;      // 로그인 아이디
  private String password;
  private String role;

  private String name;          // 실제 이름
  private String phoneNumber;
  private String email;

  @ManyToOne
  @JoinColumn(name = "hospital_id")
  private HospitalEntity hospital;

}
