package org.zerobase.hospitalappointmentproject.domain.medicalrecord.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.entity.MedicalRecordEntity;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordDto {

  private Long id;

  private String patientName;
  private LocalDate patientBirthDate;
  private String doctorName;

  private LocalDate consultationDate;

  private String diagnosis;
  private String treatment;
  private String prescription;

  public static MedicalRecordDto toDto(MedicalRecordEntity entity) {

    return MedicalRecordDto.builder()
        .id(entity.getId())
        .patientName(entity.getPatient().getName())
        .patientBirthDate(entity.getPatient().getBirthDate())
        .doctorName(entity.getDoctor().getName())
        .consultationDate(entity.getConsultationDate())
        .diagnosis(entity.getDiagnosis())
        .treatment(entity.getTreatment())
        .prescription(entity.getPrescription())
        .build();

  }

}
