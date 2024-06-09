package org.zerobase.hospitalappointmentproject.domain.medicalrecord.document;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder(toBuilder = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "hospital")
public class MedicalRecordDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Date, format = DateFormat.date)
  private LocalDate consultationDate;

  @Field(type = FieldType.Text)
  private String diagnosis;

  @Field(type = FieldType.Text)
  private String treatment;

  @Field(type = FieldType.Text)
  private String prescription;

  @Field(type = FieldType.Long)
  private Long patientId;

  @Field(type = FieldType.Long)
  private Long doctorId;

}
