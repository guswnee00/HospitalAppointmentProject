package org.zerobase.hospitalappointmentproject.domain.hospital.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalTime;
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

@SuperBuilder(toBuilder = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "hospital")
public class HospitalDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String name;

  @Field(type = FieldType.Text)
  private String address;

  @Field(type = FieldType.Double)
  private Double latitude;

  @Field(type = FieldType.Double)
  private Double longitude;

  @Field(type = FieldType.Text)
  private String contactNumber;

  @Field(type = FieldType.Text)
  private String description;

  @Field(type = FieldType.Date, format = DateFormat.time)
  @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
  private LocalTime openTime;

  @Field(type = FieldType.Date, format = DateFormat.time)
  @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
  private LocalTime closeTime;

  @Field(type = FieldType.Date, format = DateFormat.time)
  @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
  private LocalTime lunchStartTime;

  @Field(type = FieldType.Date, format = DateFormat.time)
  @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
  private LocalTime lunchEndTime;

  @Field(type = FieldType.Keyword)
  private Set<Long> doctors;

  @Field(type = FieldType.Keyword)
  private Set<Long> appointments;

}
