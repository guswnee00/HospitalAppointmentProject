package org.zerobase.hospitalappointmentproject.domain.hospital.document;

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
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

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

//  @Field(type = FieldType.Double)
//  private Double latitude;
//
//  @Field(type = FieldType.Double)
//  private Double longitude;

  @GeoPointField
  private GeoPoint location;

  @Field(type = FieldType.Text)
  private String contactNumber;

  @Field(type = FieldType.Text)
  private String description;

  @Field(type = FieldType.Date, format = DateFormat.hour_minute_second)
  private LocalTime openTime;

  @Field(type = FieldType.Date, format = DateFormat.hour_minute_second)
  private LocalTime closeTime;

  @Field(type = FieldType.Date, format = DateFormat.hour_minute_second)
  private LocalTime lunchStartTime;

  @Field(type = FieldType.Date, format = DateFormat.hour_minute_second)
  private LocalTime lunchEndTime;

  @Field(type = FieldType.Text)
  private Set<String> specialties;

}
