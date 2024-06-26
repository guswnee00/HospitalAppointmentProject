package org.zerobase.hospitalappointmentproject.domain.medicalrecord.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {

  Optional<MedicalRecordEntity> findByIdAndDoctor(Long id, DoctorEntity doctor);
  Page<MedicalRecordEntity> findAllByDoctor(DoctorEntity doctor, Pageable pageable);
  Page<MedicalRecordEntity> findAllByPatient(PatientEntity patient, Pageable pageable);

}
