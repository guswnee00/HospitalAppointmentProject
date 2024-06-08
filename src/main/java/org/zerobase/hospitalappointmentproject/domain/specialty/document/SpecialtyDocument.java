package org.zerobase.hospitalappointmentproject.domain.specialty.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "specialty")
public class SpecialtyDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String name;

}
