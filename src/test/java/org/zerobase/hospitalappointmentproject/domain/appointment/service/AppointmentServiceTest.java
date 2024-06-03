package org.zerobase.hospitalappointmentproject.domain.appointment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.BREAK_TIME_FOR_LUNCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_IS_NOT_AVAILABLE;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.DOCTOR_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.NOT_HOSPITAL_OPERATING_HOUR;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PATIENT_NOT_FOUND;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentCreate;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentDto;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.appointment.repository.AppointmentRepository;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.domain.specialty.entity.SpecialtyEntity;
import org.zerobase.hospitalappointmentproject.global.common.Hour;
import org.zerobase.hospitalappointmentproject.global.common.Minute;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

  @Mock
  private AppointmentRepository appointmentRepository;
  @Mock
  private PatientRepository patientRepository;
  @Mock
  private HospitalRepository hospitalRepository;
  @Mock
  private DoctorRepository doctorRepository;

  @InjectMocks
  private AppointmentService appointmentService;

  @Test
  void successCreateAppointment() {

    String username = "testPatient";
    AppointmentCreate.Request request = new AppointmentCreate.Request();
    request.setHospitalName("testHospital");
    request.setSpecialtyName("testSpecialty");
    request.setDoctorName("testDoctor");
    request.setAppointmentDate(LocalDate.now());
    request.setAppointmentHour(Hour.H09);
    request.setAppointmentMinute(Minute.M00);

    PatientEntity patient = PatientEntity.builder()
        .username(username).build();

    HospitalEntity hospital = HospitalEntity.builder()
        .name(request.getHospitalName())
        .openTime(LocalTime.of(9, 0))
        .closeTime(LocalTime.of(17, 0))
        .lunchStartTime(LocalTime.of(12, 0))
        .lunchEndTime(LocalTime.of(13, 0)).build();


    SpecialtyEntity specialty = SpecialtyEntity.builder()
        .name(request.getSpecialtyName()).build();

    DoctorEntity doctor = DoctorEntity.builder()
        .name(request.getDoctorName())
        .specialty(specialty)
        .hospital(hospital).build();

    AppointmentEntity appointment = AppointmentEntity.builder()
        .patient(patient)
        .hospital(hospital)
        .doctor(doctor)
        .appointmentDate(request.getAppointmentDate())
        .appointmentTime(LocalTime.of(request.getAppointmentHour().getHour(), request.getAppointmentMinute().getMinute())).build();

    when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));
    when(hospitalRepository.findByName(request.getHospitalName())).thenReturn(Optional.of(hospital));
    when(doctorRepository.findByNameAndSpecialty_NameAndHospital(request.getDoctorName(), request.getSpecialtyName(), hospital)).thenReturn(Optional.of(doctor));
    when(appointmentRepository.findByDoctorAndAppointmentDateAndAppointmentTimeWithLock(doctor, request.getAppointmentDate(), appointment.getAppointmentTime())).thenReturn(Optional.empty());
    when(appointmentRepository.save(any(AppointmentEntity.class))).thenReturn(appointment);

    AppointmentDto appointmentDto = appointmentService.create(request, username);

    assertNotNull(appointmentDto);
    assertEquals(request.getHospitalName(), appointmentDto.getHospitalName());
    assertEquals(request.getDoctorName(), appointmentDto.getDoctorName());
    assertEquals(request.getAppointmentDate(), appointmentDto.getAppointmentDate());
    assertEquals(appointment.getAppointmentTime(), appointmentDto.getAppointmentTime());

  }

  @Test
  void failCreateAppointment_patientNotFound() {

    String username = "patientUsername";
    AppointmentCreate.Request request = new AppointmentCreate.Request();

    when(patientRepository.findByUsername(username)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> appointmentService.create(request, username));

    assertEquals(PATIENT_NOT_FOUND.getDescription(), exception.getMessage());

  }

  @Test
  void failCreateAppointment_hospitalNotFound() {

    String username = "testUsername";
    AppointmentCreate.Request request = new AppointmentCreate.Request();
    request.setHospitalName("testHospital");

    PatientEntity patient = PatientEntity.builder()
        .username(username).build();

    when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));
    when(hospitalRepository.findByName(request.getHospitalName())).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> appointmentService.create(request, username));

    assertEquals(HOSPITAL_NOT_FOUND.getDescription(), exception.getMessage());
  }

  @Test
  void failCreateAppointment_doctorNotFound() {

    String username = "testPatient";
    AppointmentCreate.Request request = new AppointmentCreate.Request();
    request.setHospitalName("testHospital");
    request.setSpecialtyName("testSpecialty");
    request.setDoctorName("testDoctor");

    PatientEntity patient = PatientEntity.builder()
        .username(username).build();

    HospitalEntity hospital = HospitalEntity.builder()
        .name(request.getHospitalName()).build();

    when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));
    when(hospitalRepository.findByName(request.getHospitalName())).thenReturn(Optional.of(hospital));
    when(doctorRepository.findByNameAndSpecialty_NameAndHospital(request.getDoctorName(), request.getSpecialtyName(), hospital)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class,
        () -> appointmentService.create(request, username));

    assertEquals(DOCTOR_NOT_FOUND.getDescription(), exception.getMessage());

  }

  @Test
  void failCreateAppointment_doctorNotAvailable() {

    String username = "testPatient";
    AppointmentCreate.Request request = new AppointmentCreate.Request();
    request.setHospitalName("testHospital");
    request.setSpecialtyName("testSpecialty");
    request.setDoctorName("testDoctor");
    request.setAppointmentDate(LocalDate.now());
    request.setAppointmentHour(Hour.H09);
    request.setAppointmentMinute(Minute.M00);

    PatientEntity patient = PatientEntity.builder().username(username).build();

    HospitalEntity hospital = HospitalEntity.builder().name(request.getHospitalName())
        .openTime(LocalTime.of(8, 0))
        .closeTime(LocalTime.of(18, 0))
        .lunchStartTime(LocalTime.of(12, 0))
        .lunchEndTime(LocalTime.of(13, 0))
        .build();

    SpecialtyEntity specialty = SpecialtyEntity.builder().name(request.getSpecialtyName()).build();

    DoctorEntity doctor = DoctorEntity.builder().name(request.getDoctorName()).specialty(specialty)
        .hospital(hospital).build();

    LocalTime appointmentTime = LocalTime.of(request.getAppointmentHour().getHour(),
        request.getAppointmentMinute().getMinute());

    when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));
    when(hospitalRepository.findByName(request.getHospitalName())).thenReturn(Optional.of(hospital));
    when(doctorRepository.findByNameAndSpecialty_NameAndHospital(request.getDoctorName(), request.getSpecialtyName(), hospital))
        .thenReturn(Optional.of(doctor));
    when(appointmentRepository.findByDoctorAndAppointmentDateAndAppointmentTimeWithLock(doctor, request.getAppointmentDate(), appointmentTime))
        .thenReturn(Optional.of(AppointmentEntity.builder().doctor(doctor).appointmentDate(request.getAppointmentDate()).appointmentTime(appointmentTime).build()));

    CustomException exception = assertThrows(CustomException.class,
        () -> appointmentService.create(request, username));


    assertEquals(DOCTOR_IS_NOT_AVAILABLE.getDescription(), exception.getMessage());

  }

  @Test
  void failCreateAppointment_notHospitalOperatingHour() {

    String username = "testPatient";
    AppointmentCreate.Request request = new AppointmentCreate.Request();
    request.setHospitalName("testHospital");
    request.setSpecialtyName("testSpecialty");
    request.setDoctorName("testDoctor");
    request.setAppointmentDate(LocalDate.now());
    request.setAppointmentHour(Hour.H09);
    request.setAppointmentMinute(Minute.M00);

    PatientEntity patient = PatientEntity.builder()
        .username(username).build();

    HospitalEntity hospital = HospitalEntity.builder()
        .name(request.getHospitalName())
        .openTime(LocalTime.of(10, 0))
        .closeTime(LocalTime.of(17, 0)).build();


    SpecialtyEntity specialty = SpecialtyEntity.builder()
        .name(request.getSpecialtyName()).build();

    DoctorEntity doctor = DoctorEntity.builder()
        .name(request.getDoctorName())
        .specialty(specialty)
        .hospital(hospital).build();

    when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));
    when(hospitalRepository.findByName(request.getHospitalName())).thenReturn(Optional.of(hospital));
    when(doctorRepository.findByNameAndSpecialty_NameAndHospital(request.getDoctorName(), request.getSpecialtyName(), hospital)).thenReturn(Optional.of(doctor));

    CustomException exception = assertThrows(CustomException.class, () -> appointmentService.create(request, username));

    assertEquals(NOT_HOSPITAL_OPERATING_HOUR.getDescription(), exception.getMessage());

  }

  @Test
  void failCreateAppointment_breakTimeForLunch() {

    String username = "testPatient";
    AppointmentCreate.Request request = new AppointmentCreate.Request();
    request.setHospitalName("testHospital");
    request.setSpecialtyName("testSpecialty");
    request.setDoctorName("testDoctor");
    request.setAppointmentDate(LocalDate.now());
    request.setAppointmentHour(Hour.H12);
    request.setAppointmentMinute(Minute.M30);

    PatientEntity patient = PatientEntity.builder()
        .username(username).build();

    HospitalEntity hospital = HospitalEntity.builder()
        .name(request.getHospitalName())
        .openTime(LocalTime.of(9, 0))
        .closeTime(LocalTime.of(17, 0))
        .lunchStartTime(LocalTime.of(12, 0))
        .lunchEndTime(LocalTime.of(13, 0)).build();


    SpecialtyEntity specialty = SpecialtyEntity.builder()
        .name(request.getSpecialtyName()).build();

    DoctorEntity doctor = DoctorEntity.builder()
        .name(request.getDoctorName())
        .specialty(specialty)
        .hospital(hospital).build();

    when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));
    when(hospitalRepository.findByName(request.getHospitalName())).thenReturn(Optional.of(hospital));
    when(doctorRepository.findByNameAndSpecialty_NameAndHospital(request.getDoctorName(), request.getSpecialtyName(), hospital)).thenReturn(Optional.of(doctor));

    CustomException exception = assertThrows(CustomException.class, () -> appointmentService.create(request, username));

    assertEquals(BREAK_TIME_FOR_LUNCH.getDescription(), exception.getMessage());

  }
}

