package org.zerobase.hospitalappointmentproject.domain.appointment.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

  boolean existsByDoctorAndAppointmentDateAndAppointmentTime(DoctorEntity doctor, LocalDate date, LocalTime time);

}
