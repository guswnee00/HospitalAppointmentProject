package org.zerobase.hospitalappointmentproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableElasticsearchAuditing
@EnableElasticsearchRepositories(basePackages = "org.zerobase.hospitalappointmentproject.domain.hospital.repository")
public class HospitalAppointmentProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalAppointmentProjectApplication.class, args);
    }

}
