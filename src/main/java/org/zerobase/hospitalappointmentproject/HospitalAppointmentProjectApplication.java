package org.zerobase.hospitalappointmentproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HospitalAppointmentProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalAppointmentProjectApplication.class, args);
    }

}
