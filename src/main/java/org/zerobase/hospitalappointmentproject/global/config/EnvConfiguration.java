package org.zerobase.hospitalappointmentproject.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${PWD}/.env")
public class EnvConfiguration {

}
