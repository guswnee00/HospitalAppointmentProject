package org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto;

import java.time.LocalDate;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;

public class MedicalRecordUpdate {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private String diagnosis;
    private String treatment;
    private String prescription;

    public MedicalRecordEntity toUpdateEntity(MedicalRecordEntity entity) {

      MedicalRecordEntity.MedicalRecordEntityBuilder builder = entity.toBuilder();

      Optional.ofNullable(this.diagnosis).ifPresent(builder::diagnosis);
      Optional.ofNullable(this.treatment).ifPresent(builder::treatment);
      Optional.ofNullable(this.prescription).ifPresent(builder::prescription);

      return builder.build();

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

    public static MedicalRecordCreate.Response fromDto(MedicalRecordDto dto) {

      return MedicalRecordCreate.Response.builder()
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
