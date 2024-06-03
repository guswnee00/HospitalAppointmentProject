package org.zerobase.hospitalappointmentproject.domain.appointment.service;

import static org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus.CONFIRMED_APPOINTMENT;
import static org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus.NO_SHOW;
import static org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus.PENDING_APPOINTMENT;
import static org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus.WAITING_CONSULTATION;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.APPOINTMENT_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.ARRIVAL_CONFIRMATION_TIME_HAS_PASSED;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.BREAK_TIME_FOR_LUNCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CANNOT_CANCEL;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CANNOT_MODIFICATION;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CHECK_YOUR_APPOINTMENT_STATUS;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_IS_NOT_AVAILABLE;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.NOT_HOSPITAL_OPERATING_HOUR;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PATIENT_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.STAFF_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.TODAY_IS_NOT_A_APPOINTMENT_DAY;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.common.Hour;
import org.zerobase.hospitalappointmentproject.global.common.Minute;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final PatientRepository patientRepository;
  private final HospitalRepository hospitalRepository;
  private final DoctorRepository doctorRepository;
  private final StaffRepository staffRepository;

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
    DoctorEntity doctor = doctorRepository.findByNameAndSpecialty_NameAndHospital(request.getDoctorName(), request.getSpecialtyName(), hospital)
                                          .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));

    LocalTime appointmentTime = LocalTime.of(request.getAppointmentHour().getHour(), request.getAppointmentMinute().getMinute());

    checkValidTime(appointmentTime, hospital);

    if (appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, request.getAppointmentDate(), appointmentTime)) {
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

    PatientEntity patient = patientRepository.findByUsername(username)
                                             .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));
    AppointmentEntity appointment = appointmentRepository.findByIdAndPatient(appointmentId, patient)
                                                         .orElseThrow(() -> new CustomException(APPOINTMENT_NOT_FOUND));
    HospitalEntity hospital = request.getHospitalName() != null ?
                                      hospitalRepository.findByName(request.getHospitalName())
                                      .orElseThrow(() -> new CustomException(HOSPITAL_NOT_FOUND))
                                      : appointment.getHospital();
    DoctorEntity doctor = request.getDoctorName() != null ?
                                  doctorRepository.findByNameAndSpecialty_NameAndHospital(request.getDoctorName(), request.getSpecialtyName(), hospital)
                                  .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND))
                                  : appointment.getDoctor();

    if (appointment.getStatus() != PENDING_APPOINTMENT) {
      throw new CustomException(CANNOT_MODIFICATION);
    }


    LocalTime appointmentTime = LocalTime.of(
        Optional.ofNullable(request.getAppointmentHour()).map(Hour::getHour).orElse(appointment.getAppointmentTime().getHour()),
        Optional.ofNullable(request.getAppointmentMinute()).map(Minute::getMinute).orElse(appointment.getAppointmentTime().getMinute())
    );
    checkValidTime(appointmentTime, hospital);

    if (appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, request.getAppointmentDate(), appointmentTime)) {
      throw new CustomException(DOCTOR_IS_NOT_AVAILABLE);
    }

    AppointmentEntity updateAppointment = appointmentRepository.save(request.toUpdateEntity(appointment, hospital, doctor));

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

    PatientEntity patient = patientRepository.findByUsername(username)
                                             .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));
    AppointmentEntity appointment = appointmentRepository.findByIdAndPatient(appointmentId, patient)
                                                         .orElseThrow(() -> new CustomException(APPOINTMENT_NOT_FOUND));

    if (appointment.getStatus() != PENDING_APPOINTMENT) {
      throw new CustomException(CANNOT_CANCEL);
    }

    appointmentRepository.delete(appointment);
  }

  /**
   * 환자의 예약 목록 확인
   */
  public Page<AppointmentDto> patientAppointments(String username, Pageable pageable) {

    PatientEntity patient = patientRepository.findByUsername(username)
                                             .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));

    return appointmentRepository.findByPatient(patient, pageable).map(AppointmentDto::toDto);

  }

  /**
   * 병원 관계자의 예약 목록 확인
   */
  public Page<AppointmentDto> staffAppointments(String username, Pageable pageable) {

    StaffEntity staff = staffRepository.findByUsername(username)
                                       .orElseThrow(() -> new CustomException(STAFF_NOT_FOUND));

    HospitalEntity hospital = staff.getHospital();

    return appointmentRepository.findByHospital(hospital, pageable).map(AppointmentDto::toDto);

  }

  /**
   * 예약 확정 처리
   *    모든 병원들이 오전 9시에 '예약 확정' 처리를 자동으로 할 수 있도록 scheduling
   */
  @Transactional
  public void confirmAppointmentsForAllHospitals() {

    LocalDate today = LocalDate.now();
    LocalDate confirmationDate = today.plusDays(3);

    List<AppointmentEntity> appointments = appointmentRepository.findByStatusAndAppointmentDateLessThanEqual(PENDING_APPOINTMENT, confirmationDate);

    appointmentRepository .saveAll(appointments.stream()
        .map(appointment -> appointment.toBuilder().status(CONFIRMED_APPOINTMENT).build())
        .collect(Collectors.toList()));

  }

  /**
   * 환자의 병원 도착 확인
   *    1. 환자 아이디와 예야 예약 아이디를 통해 예약 내역 가져오기
   *    2. 도착 확인
   *        A. 예약 날짜 확인
   *        B. 예약 시간 15분 전 도착 확인
   *        C. 예약 상태가 '예약 확정' 상태인지 확인
   *    3. 확인이 끝나면 '진료 대기' 상태로 예약 상태 변경 후 저장
   */
  @Transactional
  public void patientArrival(String username, Long appointmentId) {

    PatientEntity patient = patientRepository.findByUsername(username)
                                             .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));
    AppointmentEntity appointment = appointmentRepository.findByIdAndPatient(appointmentId, patient)
                                                         .orElseThrow(() -> new CustomException(APPOINTMENT_NOT_FOUND));

    checkPatientArrival(appointment);

    appointmentRepository.save(appointment.toBuilder().status(WAITING_CONSULTATION).build());

  }

  private void checkPatientArrival(AppointmentEntity appointment) {

    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();

    if (!appointment.getAppointmentDate().equals(today)) {
      throw new CustomException(TODAY_IS_NOT_A_APPOINTMENT_DAY);
    }

    LocalTime appointmentTime = appointment.getAppointmentTime();
    if (now.isBefore(appointmentTime.minusMinutes(15))) {
      appointmentRepository.save(appointment.toBuilder()
                                            .status(NO_SHOW).build());
      throw new CustomException(ARRIVAL_CONFIRMATION_TIME_HAS_PASSED);
    }

    if (appointment.getStatus() != CONFIRMED_APPOINTMENT) {
      throw new CustomException(CHECK_YOUR_APPOINTMENT_STATUS);
    }

  }

}
