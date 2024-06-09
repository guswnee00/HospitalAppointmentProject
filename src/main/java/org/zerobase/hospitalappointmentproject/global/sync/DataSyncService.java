package org.zerobase.hospitalappointmentproject.global.sync;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.appointment.document.AppointmentDocument;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;
import org.zerobase.hospitalappointmentproject.domain.appointment.repository.AppointmentElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.appointment.repository.AppointmentRepository;
import org.zerobase.hospitalappointmentproject.domain.doctor.document.DoctorDocument;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.document.MedicalRecordDocument;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.repository.MedicalRecordElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.repository.MedicalRecordRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.document.PatientDocument;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.domain.specialty.document.SpecialtyDocument;
import org.zerobase.hospitalappointmentproject.domain.specialty.entity.SpecialtyEntity;
import org.zerobase.hospitalappointmentproject.domain.specialty.repository.SpecialtyElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.specialty.repository.SpecialtyRepository;
import org.zerobase.hospitalappointmentproject.domain.staff.document.StaffDocument;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;

@Service
@RequiredArgsConstructor
public class DataSyncService {

  private final PatientRepository patientRepository;
  private final StaffRepository staffRepository;
  private final DoctorRepository doctorRepository;
  private final SpecialtyRepository specialtyRepository;
  private final AppointmentRepository appointmentRepository;
  private final HospitalRepository hospitalRepository;
  private final MedicalRecordRepository medicalRecordRepository;

  private final PatientElasticRepository patientElasticRepository;
  private final StaffElasticRepository staffElasticRepository;
  private final DoctorElasticRepository doctorElasticRepository;
  private final SpecialtyElasticRepository specialtyElasticRepository;
  private final AppointmentElasticRepository appointmentElasticRepository;
  private final HospitalElasticRepository hospitalElasticRepository;
  private final MedicalRecordElasticRepository medicalRecordElasticRepository;

  @Transactional(readOnly = true)
  public void syncAll() {

  }

  public void syncPatients() {

    List<PatientEntity> patientEntities = patientRepository.findAll();
    List<PatientDocument> patientDocuments = patientEntities.stream()
                                                            .map(this::convertPatients)
                                                            .toList();
    patientElasticRepository.saveAll(patientDocuments);

  }

  public void syncStaffs() {

    List<StaffEntity> staffEntities = staffRepository.findAll();
    List<StaffDocument> staffDocuments = staffEntities.stream()
                                                      .map(this::convertStaffs)
                                                      .toList();
    staffElasticRepository.saveAll(staffDocuments);

  }

  public void syncDoctors() {

  }

  public void syncSpecialties() {

  }

  public void syncAppointments() {

  }

  public void syncHospitals() {

  }

  public void syncMedicalRecords() {

  }

  private PatientDocument convertPatients(PatientEntity patient) {

    return PatientDocument.builder()
        .id(patient.getId())
        .username(patient.getUsername())
        .password(patient.getPassword())
        .role(patient.getRole())
        .name(patient.getName())
        .phoneNumber(patient.getPhoneNumber())
        .email(patient.getEmail())
        .gender(patient.getGender())
        .birthDate(patient.getBirthDate())
        .address(patient.getAddress())
        .medicalRecords(Optional.ofNullable(patient.getMedicalRecords())
                                .orElse(Collections.emptySet())
                                .stream()
                                .map(MedicalRecordEntity::getId)
                                .collect(Collectors.toSet()))
        .appointments(Optional.ofNullable(patient.getAppointments())
                              .orElse(Collections.emptySet())
                              .stream()
                              .map(AppointmentEntity::getId)
                              .collect(Collectors.toSet()))
        .createdAt(patient.getCreatedAt())
        .modifiedAt(patient.getModifiedAt())
        .build();

  }

  private StaffDocument convertStaffs(StaffEntity staff) {

    return StaffDocument.builder()
        .id(staff.getId())
        .username(staff.getUsername())
        .password(staff.getPassword())
        .role(staff.getRole())
        .name(staff.getName())
        .phoneNumber(staff.getPhoneNumber())
        .email(staff.getEmail())
        .hospitalId(Optional.ofNullable(staff.getHospital())
                            .map(HospitalEntity::getId)
                            .orElse(null))
        .createdAt(staff.getCreatedAt())
        .modifiedAt(staff.getModifiedAt())
        .build();

  }

  private DoctorDocument convertDoctors(DoctorEntity doctor) {

    return DoctorDocument.builder()
        .id(doctor.getId())
        .username(doctor.getUsername())
        .password(doctor.getPassword())
        .role(doctor.getRole())
        .name(doctor.getName())
        .phoneNumber(doctor.getPhoneNumber())
        .email(doctor.getEmail())
        .bio(doctor.getBio())
        .specialtyId(doctor.getSpecialty().getId())
        .hospitalId(doctor.getHospital().getId())
        .medicalRecords(Optional.ofNullable(doctor.getMedicalRecords())
                                .orElse(Collections.emptySet())
                                .stream()
                                .map(MedicalRecordEntity::getId)
                                .collect(Collectors.toSet()))
        .appointments(Optional.ofNullable(doctor.getAppointments())
                              .orElse(Collections.emptySet())
                              .stream()
                              .map(AppointmentEntity::getId)
                              .collect(Collectors.toSet()))
        .createdAt(doctor.getCreatedAt())
        .modifiedAt(doctor.getModifiedAt())
        .build();

  }

  private SpecialtyDocument convertSpecialties(SpecialtyEntity specialty) {

    return SpecialtyDocument.builder().build();

  }

  private AppointmentDocument convertAppointments(AppointmentEntity appointment) {

    return AppointmentDocument.builder().build();

  }

  private HospitalDocument convertHospitals(HospitalEntity hospital) {

    return HospitalDocument.builder().build();

  }

  private MedicalRecordDocument convertMedicalRecords(MedicalRecordEntity medicalRecord) {

    return MedicalRecordDocument.builder().build();

  }

}
