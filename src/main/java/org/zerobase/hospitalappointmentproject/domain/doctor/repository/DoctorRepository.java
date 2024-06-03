package org.zerobase.hospitalappointmentproject.domain.doctor.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {

  Optional<DoctorEntity> findByUsername(String username);
  Optional<DoctorEntity> findByNameAndSpecialty_NameAndHospital(String name, String specialtyName, HospitalEntity hospital);

}
