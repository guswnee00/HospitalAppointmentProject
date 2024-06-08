package org.zerobase.hospitalappointmentproject.domain.appointment.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.zerobase.hospitalappointmentproject.domain.doctor.document.DoctorDocument;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;
import org.zerobase.hospitalappointmentproject.global.common.AppointmentStatus;

@SuperBuilder(toBuilder = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "appointment")
public class AppointmentDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Date, format = DateFormat.date)
  private LocalDate appointmentDate;

  @Field(type = FieldType.Date, format = DateFormat.time)
  @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
  private LocalTime appointmentTime;

  @Field(type = FieldType.Keyword)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private AppointmentStatus status;

  @Field(type = FieldType.Date, format = DateFormat.date_time)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @Field(type = FieldType.Date, format = DateFormat.date_time)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime modifiedAt;

  @Field(type = FieldType.Nested)
  private DoctorDocument doctor;

  @Field(type = FieldType.Nested)
  private HospitalDocument hospital;

}
