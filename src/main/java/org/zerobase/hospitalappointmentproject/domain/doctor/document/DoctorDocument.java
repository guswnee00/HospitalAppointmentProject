package org.zerobase.hospitalappointmentproject.domain.doctor.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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

  @Field(type = FieldType.Text)
  private String bio;

  @Field(type = FieldType.Nested)
  private SpecialtyDocument specialty;

  @Field(type = FieldType.Nested)
  private HospitalDocument hospital;

  @Field(type = FieldType.Nested)
  private Set<MedicalRecordDocument> medicalRecords;

  @Field(type = FieldType.Nested)
  private Set<AppointmentDocument> appointments;

  @Field(type = FieldType.Date, format = DateFormat.date_time)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @Field(type = FieldType.Date, format = DateFormat.date_time)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime modifiedAt;

}
