package org.zerobase.hospitalappointmentproject.domain.medicalrecord.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.MEDICAL_RECORD_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PATIENT_NOT_FOUND;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.appointment.repository.AppointmentRepository;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordCreate;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordDto;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto.MedicalRecordUpdate;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.repository.MedicalRecordRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {
  @Mock
  private MedicalRecordRepository medicalRecordRepository;
  @Mock
  private DoctorRepository doctorRepository;
  @Mock
  private PatientRepository patientRepository;
  @Mock
  private AppointmentRepository appointmentRepository;

  @InjectMocks
  private MedicalRecordService medicalRecordService;

  private DoctorEntity doctor;
  private PatientEntity patient;
  private AppointmentEntity appointment;
  private MedicalRecordEntity medicalRecord;

  @BeforeEach
  void init() {
    doctor = DoctorEntity.builder().id(1L).username("doctorUserName").build();
    patient = PatientEntity.builder().id(1L).username("patientUserName").name("testName").birthDate(LocalDate.of(2024, 1, 1)).build();
    appointment = AppointmentEntity.builder().id(1L).appointmentDate(LocalDate.now()).doctor(doctor).patient(patient).status(AppointmentStatus.WAITING_CONSULTATION).build();
    medicalRecord = MedicalRecordEntity.builder().id(1L).consultationDate(LocalDate.now()).doctor(doctor).patient(patient).build();
  }

  @Test
  void successCreate() {

    MedicalRecordCreate.Request request = new MedicalRecordCreate.Request();
    request.setPatientName(patient.getName());
    request.setPatientBirthDate(patient.getBirthDate());
    request.setConsultationDate(LocalDate.now());

    when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.of(doctor));
    when(patientRepository.findByNameAndBirthDate(patient.getName(), patient.getBirthDate())).thenReturn(Optional.of(patient));
    when(medicalRecordRepository.save(any(MedicalRecordEntity.class))).thenReturn(medicalRecord);
    when(appointmentRepository.findByPatientAndDoctorAndAppointmentDate(patient, doctor, request.getConsultationDate())).thenReturn(Optional.of(appointment));

    MedicalRecordDto medicalRecordDto = medicalRecordService.create(request, doctor.getUsername());

    assertNotNull(medicalRecordDto);
    assertEquals(medicalRecord.getId(), medicalRecordDto.getId());
    verify(appointmentRepository).save(any(AppointmentEntity.class));

  }

  @Test
  void failCreate_DoctorNotFound() {

    MedicalRecordCreate.Request request = new MedicalRecordCreate.Request();
    request.setPatientName(patient.getName());
    request.setPatientBirthDate(patient.getBirthDate());
    request.setConsultationDate(LocalDate.now());

    when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> medicalRecordService.create(request, doctor.getUsername()));

    assertEquals(DOCTOR_NOT_FOUND.getDescription(), exception.getMessage());

  }

  @Test
  void failCreate_PatientNotFound() {

    MedicalRecordCreate.Request request = new MedicalRecordCreate.Request();
    request.setPatientName(patient.getName());
    request.setPatientBirthDate(patient.getBirthDate());
    request.setConsultationDate(LocalDate.now());

    when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.of(doctor));
    when(patientRepository.findByNameAndBirthDate(patient.getName(), patient.getBirthDate())).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> medicalRecordService.create(request, doctor.getUsername()));

    assertEquals(PATIENT_NOT_FOUND.getDescription(), exception.getMessage());

  }

  @Test
  void successUpdate() {

    MedicalRecordUpdate.Request request = new MedicalRecordUpdate.Request();
    request.setDiagnosis("Updated Diagnosis");

    when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.of(doctor));
    when(medicalRecordRepository.findByIdAndDoctor(medicalRecord.getId(), doctor)).thenReturn(Optional.of(medicalRecord));
    when(medicalRecordRepository.save(any(MedicalRecordEntity.class))).thenReturn(medicalRecord);

    MedicalRecordDto medicalRecordDto = medicalRecordService.update(request, doctor.getUsername(), medicalRecord.getId());

    assertNotNull(medicalRecordDto);
    assertEquals(medicalRecord.getId(), medicalRecordDto.getId());
    verify(medicalRecordRepository).save(any(MedicalRecordEntity.class));

  }

  @Test
  void failUpdate_DoctorNotFound() {

    MedicalRecordUpdate.Request request = new MedicalRecordUpdate.Request();
    request.setDiagnosis("Updated Diagnosis");

    when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> medicalRecordService.update(request, doctor.getUsername(), medicalRecord.getId()));

    assertEquals(DOCTOR_NOT_FOUND.getDescription(), exception.getMessage());

  }

  @Test
  void failUpdate_RecordNotFound() {

    MedicalRecordUpdate.Request request = new MedicalRecordUpdate.Request();
    request.setDiagnosis("Updated Diagnosis");

    when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.of(doctor));
    when(medicalRecordRepository.findByIdAndDoctor(medicalRecord.getId(), doctor)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> medicalRecordService.update(request, doctor.getUsername(), medicalRecord.getId()));

    assertEquals(MEDICAL_RECORD_NOT_FOUND.getDescription(), exception.getMessage());

  }

  @Test
  void successDoctorMedicalRecords() {

    Pageable pageable = PageRequest.of(0, 10);
    Page<MedicalRecordEntity> page = new PageImpl<>(List.of(medicalRecord));

    when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.of(doctor));
    when(medicalRecordRepository.findAllByDoctor(doctor, pageable)).thenReturn(page);

    Page<MedicalRecordDto> medicalRecordDtos = medicalRecordService.doctorMedicalRecords(doctor.getUsername(), pageable);

    assertNotNull(medicalRecordDtos);
    assertEquals(1, medicalRecordDtos.getTotalElements());
    verify(medicalRecordRepository).findAllByDoctor(doctor, pageable);

  }

  @Test
  void failDoctorMedicalRecords_DoctorNotFound() {

    Pageable pageable = PageRequest.of(0, 10);

    when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> medicalRecordService.doctorMedicalRecords(doctor.getUsername(), pageable));

    assertEquals(DOCTOR_NOT_FOUND.getDescription(), exception.getMessage());

  }

  @Test
  void successPatientMedicalRecords() {

    Pageable pageable = PageRequest.of(0, 10);
    Page<MedicalRecordEntity> page = new PageImpl<>(List.of(medicalRecord));

    when(patientRepository.findByUsername(patient.getUsername())).thenReturn(Optional.of(patient));
    when(medicalRecordRepository.findAllByPatient(patient, pageable)).thenReturn(page);

    Page<MedicalRecordDto> medicalRecordDtos = medicalRecordService.patientMedicalRecords(patient.getUsername(), pageable);

    assertNotNull(medicalRecordDtos);
    assertEquals(1, medicalRecordDtos.getTotalElements());
    verify(medicalRecordRepository).findAllByPatient(patient, pageable);

  }

  @Test
  void failPatientMedicalRecords_PatientNotFound() {

    Pageable pageable = PageRequest.of(0, 10);

    when(patientRepository.findByUsername(patient.getUsername())).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> medicalRecordService.patientMedicalRecords(patient.getUsername(), pageable));

    assertEquals(PATIENT_NOT_FOUND.getDescription(), exception.getMessage());
  }

}