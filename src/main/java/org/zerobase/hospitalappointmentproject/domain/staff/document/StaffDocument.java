package org.zerobase.hospitalappointmentproject.domain.staff.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;

@SuperBuilder(toBuilder = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "staff")
public class StaffDocument {

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

  @Field(type = FieldType.Nested)
  private HospitalEntity hospital;

  @Field(type = FieldType.Date, format = DateFormat.date_time)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @Field(type = FieldType.Date, format = DateFormat.date_time)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime modifiedAt;

}