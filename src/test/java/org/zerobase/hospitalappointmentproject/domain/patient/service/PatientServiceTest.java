package org.zerobase.hospitalappointmentproject.domain.patient.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientDto;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientSignup;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.mapper.PatientMapper;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

  @Mock
  private PatientRepository patientRepository;
  @Mock
  private PatientMapper patientMapper;
  @Mock
  private UserValidationService userValidationService;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private PatientService patientService;

  private PatientSignup.Request request;
  private PatientEntity patientEntity;
  private PatientDto patientDto;

  @BeforeEach
  void init() {

    request = new PatientSignup.Request();
    request.setUsername("testUsername");
    request.setPassword("testPassword");
    request.setCheckingPassword("testPassword");
    request.setGender("M");
    request.setBirthYear(2024);
    request.setBirthMonth(1);
    request.setBirthDay(1);

    patientEntity = PatientEntity.builder()
        .username("testUsername")
        .build();

    patientDto = new PatientDto();
    patientDto.setUsername("testUsername");

  }

  @Test
  void successSignup() {

    try(MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {

      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword")).thenReturn(true);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword("testPassword")).thenReturn(true);
      when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("encodedPassword");
      when(patientRepository.save(any(PatientEntity.class))).thenReturn(patientEntity);
      when(patientMapper.toDto(patientEntity)).thenReturn(patientDto);

      PatientDto patientDto = patientService.signup(request);

      assertNotNull(patientDto);
      assertEquals("testUsername", patientDto.getUsername());

    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    when(userValidationService.isUsernameUsed("testUsername")).thenReturn(true);

    CustomException exception = assertThrows(CustomException.class,
        () -> patientService.signup(request));

    assertEquals("이미 사용중인 아이디 입니다.", exception.getErrorMessage());

  }

  @Test
  void failSignup_TwoPasswordDoNotMatch() {

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword2")).thenReturn(false);

      request.setCheckingPassword("testPassword2");

      CustomException exception = assertThrows(CustomException.class,
          () -> patientService.signup(request));

      assertEquals("비밀번호와 확인용 비밀번호가 일치하지 않습니다.", exception.getErrorMessage());

    }

  }

  @Test
  void failSignup_InvalidPassword() {
    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword")).thenReturn(true);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword("testPassword")).thenReturn(false);

      CustomException exception = assertThrows(CustomException.class,
          () -> patientService.signup(request));

      assertEquals("올바르지 않은 비밀번호입니다.", exception.getErrorMessage());
    }
  }



}