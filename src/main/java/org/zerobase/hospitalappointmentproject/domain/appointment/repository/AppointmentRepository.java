package org.zerobase.hospitalappointmentproject.domain.appointment.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

  boolean existsByDoctorAndAppointmentDateAndAppointmentTime(DoctorEntity doctor, LocalDate date, LocalTime time);
  Optional<AppointmentEntity> findByIdAndPatient(Long id, PatientEntity patient);
  Page<AppointmentEntity> findByPatient(PatientEntity patient, Pageable pageable);
  Page<AppointmentEntity> findByHospital(HospitalEntity hospital, Pageable pageable);
  List<AppointmentEntity> findByHospitalAndStatusAndAppointmentDateLessThanEqual(HospitalEntity hospital, AppointmentStatus status, LocalDate date);

}
