package org.zerobase.hospitalappointmentproject.global.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
  private PatientEntity patientEntity;

  @BeforeEach
  void init() {
    loginDto = new LoginDto();
    loginDto.setUsername("testUsername");
    loginDto.setPassword("testPassword");

    patientEntity = new PatientEntity();
    patientEntity.setUsername("testUsername");
    patientEntity.setPassword("$2a$10$DOWSDu7lH8/ZZ7K8oSkR.e5ECIU/8oeHZV5bQf1Jhzl/3fBzC9q2K");
  }

  @Test
  void successLogin() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(patientEntity);
    when(bCryptPasswordEncoder.matches("testPassword", patientEntity.getPassword())).thenReturn(true);

    PatientEntity patient = loginService.patientLogin(loginDto);

    assertNotNull(patient);
    assertEquals("testUsername", patient.getUsername());

  }

  @Test
  void failLogin_UsernameDoesNotExist() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(null);

    CustomException exception = assertThrows(CustomException.class,
        () -> loginService.patientLogin(loginDto));

    assertEquals("존재하지 않는 아이디 입니다.", exception.getErrorMessage());

  }

  @Test
  void failLogin_PasswordDoesNotExist() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(patientEntity);
    when(bCryptPasswordEncoder.matches("testPassword", patientEntity.getPassword())).thenReturn(false);

    CustomException exception = assertThrows(CustomException.class,
        () -> loginService.patientLogin(loginDto));

    assertEquals("비밀번호가 일치하지 않습니다.", exception.getErrorMessage());

  }

}