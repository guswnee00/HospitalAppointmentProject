package org.zerobase.hospitalappointmentproject.domain.patient.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

  Optional<PatientEntity> findByUsername(String username);
  Optional<PatientEntity> findByNameAndBirthDate(String name, LocalDate date);

}
