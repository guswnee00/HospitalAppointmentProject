package org.zerobase.hospitalappointmentproject.domain.appointment.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.APPOINTMENT_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.BREAK_TIME_FOR_LUNCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CANNOT_CANCEL;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CANNOT_MODIFICATION;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_IS_NOT_AVAILABLE;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.NOT_HOSPITAL_OPERATING_HOUR;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PATIENT_NOT_FOUND;

import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentCreate;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentDto;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentUpdate;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.appointment.repository.AppointmentRepository;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final PatientRepository patientRepository;
  private final HospitalRepository hospitalRepository;
  private final DoctorRepository doctorRepository;

  /**
   * 환자의 병원 예약 생성
   *    1. 시와 분을 가져와서 유효한 시간인지 확인
   *    2. 해당 의사의 진료타임에 다른 예약이 없는지(타임당 1건) 확인
   *    3. 예약 저장 후 dto 반환
   */
  @Transactional
  public AppointmentDto create(AppointmentCreate.Request request, String username) {

    PatientEntity patient = patientRepository.findByUsername(username)
                                             .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));
    HospitalEntity hospital = hospitalRepository.findByName(request.getHospitalName())
                                                .orElseThrow(() -> new CustomException(HOSPITAL_NOT_FOUND));
    DoctorEntity doctor = doctorRepository.findByUsername(request.getDoctorName())
                                          .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));

    LocalTime appointmentTime = LocalTime.of(request.getAppointmentHour().getHour(), request.getAppointmentMinute().getMinute());

    checkValidTime(appointmentTime, hospital);

    boolean isDoctorAvailable = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, request.getAppointmentDate(), appointmentTime);
    if (isDoctorAvailable) {
      throw new CustomException(DOCTOR_IS_NOT_AVAILABLE);
    }

    AppointmentEntity appointment = appointmentRepository.save(AppointmentCreate.Request.toEntity(request, patient, hospital, doctor));

    return AppointmentDto.toDto(appointment);

  }

  private void checkValidTime(LocalTime appointmentTime, HospitalEntity hospital) {
    if (appointmentTime.isBefore(hospital.getOpenTime()) || appointmentTime.isAfter(hospital.getCloseTime())) {
      throw new CustomException(NOT_HOSPITAL_OPERATING_HOUR);
    }

    if (!appointmentTime.isBefore(hospital.getLunchStartTime()) && !appointmentTime.isAfter(
        hospital.getLunchEndTime())) {
      throw new CustomException(BREAK_TIME_FOR_LUNCH);
    }
  }

  /**
   * 환자의 예약 변경
   *    1. 예약 상태가 '예약 대기' 상태인지 확인
   *    2. 유효한 예약 시간인지 확인
   *    3. 해당 의사의 진료타임에 다른 예약이 없는지(타임당 1건) 확인
   *    4. 예약 변경 후 dto 반환
   */
  @Transactional
  public AppointmentDto update(AppointmentUpdate.Request request, Long appointmentId, String username) {

    AppointmentEntity appointment = appointmentRepository.findByIdAndPatient_Username(appointmentId, username)
                                                         .orElseThrow(() -> new CustomException(APPOINTMENT_NOT_FOUND));
    HospitalEntity hospital = hospitalRepository.findByName(request.getHospitalName())
                                                .orElseThrow(() -> new CustomException(HOSPITAL_NOT_FOUND));
    DoctorEntity doctor = doctorRepository.findByUsername(request.getDoctorName())
                                          .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));

    if (appointment.getStatus() != AppointmentStatus.PENDING_APPOINTMENT) {
      throw new CustomException(CANNOT_MODIFICATION);
    }

    LocalTime appointmentTime = LocalTime.of(request.getAppointmentHour().getHour(), request.getAppointmentMinute().getMinute());

    checkValidTime(appointmentTime, hospital);

    boolean isDoctorAvailable = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, request.getAppointmentDate(), appointmentTime);
    if (isDoctorAvailable) {
      throw new CustomException(DOCTOR_IS_NOT_AVAILABLE);
    }

    AppointmentEntity updateAppointment = appointmentRepository.save(request.toUpdateEntity(appointment, hospitalRepository, doctorRepository));

    return AppointmentDto.toDto(updateAppointment);

  }

  /**
   * 환자의 예약 취소
   *    1. 환자의 아이디와 예약 아이디로 엔티티 가져오기
   *    2. 예약 상태가 '예약 대기'인지 확인
   *    3. 예약 삭제
   */
  @Transactional
  public void cancel(String username, Long appointmentId) {

    AppointmentEntity appointment = appointmentRepository.findByIdAndPatient_Username(appointmentId, username)
                                                         .orElseThrow(() -> new CustomException(APPOINTMENT_NOT_FOUND));

    if (appointment.getStatus() != AppointmentStatus.PENDING_APPOINTMENT) {
      throw new CustomException(CANNOT_CANCEL);
    }

    appointmentRepository.delete(appointment);
  }

}
