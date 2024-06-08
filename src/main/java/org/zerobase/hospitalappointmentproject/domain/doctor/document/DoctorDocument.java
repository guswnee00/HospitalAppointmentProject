package org.zerobase.hospitalappointmentproject.domain.doctor.document;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.zerobase.hospitalappointmentproject.domain.appointment.document.AppointmentDocument;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.document.MedicalRecordDocument;
import org.zerobase.hospitalappointmentproject.domain.specialty.document.SpecialtyDocument;

@SuperBuilder(toBuilder = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "doctor")
public class DoctorDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String bio;

  @Field(type = FieldType.Object)
  private SpecialtyDocument specialty;

  @Field(type = FieldType.Object)
  private HospitalDocument hospital;

  @Field(type = FieldType.Nested)
  private Set<MedicalRecordDocument> medicalRecords;

  @Field(type = FieldType.Nested)
  private Set<AppointmentDocument> appointments;

}
