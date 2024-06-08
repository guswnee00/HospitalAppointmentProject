package org.zerobase.hospitalappointmentproject.domain.patient.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.zerobase.hospitalappointmentproject.domain.appointment.document.AppointmentDocument;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.document.MedicalRecordDocument;
import org.zerobase.hospitalappointmentproject.global.common.GenderType;

@SuperBuilder(toBuilder = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "patient")
public class PatientDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String username;

  @Field(type = FieldType.Text)
  private String password;

  @Field(type = FieldType.Text)
  private String role;

  @Field(type = FieldType.Text)
  private String name;

  @Field(type = FieldType.Text)
  private String phoneNumber;

  @Field(type = FieldType.Text)
  private String email;

  @Field(type = FieldType.Keyword)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private GenderType gender;

  @Field(type = FieldType.Date, format = DateFormat.date)
  private LocalDate birthDate;

  @Field(type = FieldType.Text)
  private String address;

  @Field(type = FieldType.Nested)
  private Set<MedicalRecordDocument> medicalRecords;

  @Field(type = FieldType.Nested)
  private Set<AppointmentDocument> appointments;

}
