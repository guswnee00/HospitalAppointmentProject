package org.zerobase.hospitalappointmentproject.domain.medicalrecord.service;

import static org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus.COMPLETE_CONSULTATION;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PATIENT_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.appointment.repository.AppointmentRepository;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordCreate;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordDto;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.repository.MedicalRecordRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

  private final MedicalRecordRepository medicalRecordRepository;
  private final DoctorRepository doctorRepository;
  private final PatientRepository patientRepository;
  private final AppointmentRepository appointmentRepository;

  /**
   * 의사의 진료 기록 작성
   *    1. 의사 아이디로 엔티티 가져오기
   *    2. 환자 이름과 생년월일로 엔티티 가져오기
   *    3. 진료 기록 저장
   *    4. 진료 날짜, 의사, 환자 엔티티로 예약 엔티티 가져와서 '진료 완료'상태로 바꾸기
   *    5. 진료 기록 dto 반환
   */
  @Transactional
  public MedicalRecordDto create(MedicalRecordCreate.Request request, String username) {

    DoctorEntity doctor = doctorRepository.findByUsername(username)
                                          .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));
    PatientEntity patient = patientRepository.findByNameAndBirthDate(request.getPatientName(), request.getPatientBirthDate())
                                             .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));

    MedicalRecordEntity medicalRecord = medicalRecordRepository.save(MedicalRecordCreate.Request.toEntity(request, patient, doctor));

    appointmentRepository.findByPatientAndDoctorAndAndAppointmentDate(patient, doctor, request.getConsultationDate())
        .ifPresent(appointmentRepository.save(appointment -> appointment.toBuilder().status(COMPLETE_CONSULTATION).build()));

    return MedicalRecordDto.toDto(medicalRecord);

  }

}
