package org.zerobase.hospitalappointmentproject.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

  @Bean
  public OpenAPI openAPI() {

    return new OpenAPI()
        .components(new Components())
        .info(apiInfo());

  }

  private Info apiInfo() {

    return new Info()
        .title("병원 예약 서비스")
        .description("환자가 받고싶은 진료에 대한 진료과목을 선택하여 병원을 예약할 수 있는 병원 예약 서비스입니다.")
        .version("1.0.0");

  }

}
