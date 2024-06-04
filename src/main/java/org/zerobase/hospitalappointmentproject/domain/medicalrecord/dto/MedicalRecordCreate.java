package org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;

public class MedicalRecordCreate {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private String patientName;
    private LocalDate patientBirthDate;
    private String doctorName;

    private LocalDate consultationDate;

    private String diagnosis;
    private String treatment;
    private String prescription;

   public static MedicalRecordEntity toEntity(Request request, PatientEntity patient, DoctorEntity doctor) {

     return MedicalRecordEntity.builder()
         .patient(patient)
         .doctor(doctor)
         .consultationDate(LocalDate.now())
         .diagnosis(request.getDiagnosis())
         .treatment(request.getTreatment())
         .prescription(request.getPrescription())
         .build();

   }

  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private Long medicalRecordId;

    private String patientName;
    private String doctorName;

    private LocalDate consultationDate;

    private String diagnosis;
    private String treatment;
    private String prescription;

    public static Response fromDto(MedicalRecordDto dto) {

      return Response.builder()
          .medicalRecordId(dto.getId())
          .patientName(dto.getPatientName())
          .doctorName(dto.getDoctorName())
          .consultationDate(dto.getConsultationDate())
          .diagnosis(dto.getDiagnosis())
          .treatment(dto.getTreatment())
          .prescription(dto.getPrescription())
          .build();

    }

  }

}
