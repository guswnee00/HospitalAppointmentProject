package org.zerobase.hospitalappointmentproject.domain.medicalrecord.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {

  Optional<MedicalRecordEntity> findByIdAndDoctor(Long id, DoctorEntity doctor);
}
