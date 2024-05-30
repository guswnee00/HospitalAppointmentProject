package org.zerobase.hospitalappointmentproject.domain.appointment.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.BREAK_TIME_FOR_LUNCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_IS_NOT_AVAILABLE;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.NOT_HOSPITAL_OPERATING_HOUR;

import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentCreate;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentDto;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.appointment.mapper.AppointmentMapper;
import org.zerobase.hospitalappointmentproject.domain.appointment.repository.AppointmentRepository;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final PatientRepository patientRepository;
  private final HospitalRepository hospitalRepository;
  private final DoctorRepository doctorRepository;
  private final AppointmentMapper appointmentMapper;

  @Transactional
  public AppointmentDto create(AppointmentCreate.Request request, String username) {

    PatientEntity patient = patientRepository.findByUsername(username);
    HospitalEntity hospital = hospitalRepository.findByName(request.getHospitalName());
    DoctorEntity doctor = doctorRepository.findByUsername(request.getDoctorName());

    LocalTime appointmentTime = LocalTime.of(request.getAppointmentHour().getHour(), request.getAppointmentMinute().getMinute());

    if (appointmentTime.isBefore(hospital.getOpenTime()) || appointmentTime.isAfter(hospital.getCloseTime())) {
      throw new CustomException(NOT_HOSPITAL_OPERATING_HOUR);
    }

    if (!appointmentTime.isBefore(hospital.getLunchStartTime()) && !appointmentTime.isAfter(hospital.getLunchEndTime())) {
      throw new CustomException(BREAK_TIME_FOR_LUNCH);
    }

    boolean isDoctorAvailable = appointmentRepository.existsByDoctorAndAndAppointmentDateAndAndAppointmentTime(doctor, request.getAppointmentDate(), appointmentTime);
    if (isDoctorAvailable) {
      throw new CustomException(DOCTOR_IS_NOT_AVAILABLE);
    }

    AppointmentEntity appointment = appointmentRepository.save(AppointmentCreate.Request.toEntity(request, patient, hospital, doctor));

    return appointmentMapper.toDto(appointment);

  }

}
