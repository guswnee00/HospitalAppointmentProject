package org.zerobase.hospitalappointmentproject.global.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.USERNAME_DOES_NOT_EXIST;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.auth.dto.LoginDto;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

  @Mock
  private PatientRepository patientRepository;

  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private LoginService loginService;

  private LoginDto loginDto;
  private PatientEntity patient;

  @BeforeEach
  void init() {
    loginDto = new LoginDto();
    loginDto.setUsername("testUsername");
    loginDto.setPassword("testPassword");

    patient = PatientEntity.builder()
        .username("testUsername")
        .password("$2a$10$DOWSDu7lH8/ZZ7K8oSkR.e5ECIU/8oeHZV5bQf1Jhzl/3fBzC9q2K")
        .build();

  }

  @Test
  void successLogin() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.ofNullable(patient));
    when(bCryptPasswordEncoder.matches("testPassword", patient.getPassword())).thenReturn(true);

    PatientEntity patient = loginService.patientLogin(loginDto);

    assertNotNull(patient);
    assertEquals("testUsername", patient.getUsername());

  }

  @Test
  void failLogin_UsernameDoesNotExist() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> loginService.patientLogin(loginDto));

    assertEquals(USERNAME_DOES_NOT_EXIST.getDescription(), exception.getErrorMessage());

  }

  @Test
  void failLogin_PasswordDoesNotExist() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.of(patient));
    when(bCryptPasswordEncoder.matches("testPassword", patient.getPassword())).thenReturn(false);

    CustomException exception = assertThrows(CustomException.class, () -> loginService.patientLogin(loginDto));

    assertEquals(PASSWORD_DOES_NOT_MATCH.getDescription(), exception.getErrorMessage());

  }

}