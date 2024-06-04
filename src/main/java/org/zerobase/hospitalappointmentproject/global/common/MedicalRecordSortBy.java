package org.zerobase.hospitalappointmentproject.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MedicalRecordSortBy {

  CONSULTATION_DATE("consultationDate"),
  SPECIALTY_NAME("doctor.specialty.name"),
  HOSPITAL_NAME("doctor.hospital.name");

  private final String field;

  public static MedicalRecordSortBy fromField(String field) {

    for (MedicalRecordSortBy sortBy: values()) {
      if (sortBy.name().equalsIgnoreCase(field)) {
        return sortBy;
      }
    }

    return CONSULTATION_DATE;

  }

}
