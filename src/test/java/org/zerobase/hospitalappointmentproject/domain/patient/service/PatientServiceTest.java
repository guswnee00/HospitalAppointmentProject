package org.zerobase.hospitalappointmentproject.domain.patient.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.INVALID_PASSWORD;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_IS_REQUIRED_TO_UPDATE_INFO;

import java.time.LocalDate;
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
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientSignup;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;
import org.zerobase.hospitalappointmentproject.domain.patient.mapper.PatientMapper;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.common.GenderType;
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

  private PatientSignup.Request signupRequest;
  private PatientInfoUpdate.Request updateRequest;
  private PatientEntity patientEntity;
  private PatientDto patientDto;

  @BeforeEach
  void init() {

    signupRequest = new PatientSignup.Request();
    signupRequest.setUsername("testUsername");
    signupRequest.setPassword("testPassword");
    signupRequest.setCheckingPassword("testPassword");
    signupRequest.setGender("M");
    signupRequest.setBirthYear(2024);
    signupRequest.setBirthMonth(1);
    signupRequest.setBirthDay(1);

    updateRequest = new PatientInfoUpdate.Request();
    updateRequest.setCurrentPassword("testPassword");
    updateRequest.setNewPassword("newPassword1234");
    updateRequest.setName("testName");
    updateRequest.setPhoneNumber("123456789");
    updateRequest.setEmail("test@example.com");

    patientEntity = PatientEntity.builder()
        .username("testUsername")
        .password("encodedTestPassword")
        .gender(GenderType.MALE)
        .birthDate(LocalDate.of(2024, 1, 1))
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

      PatientDto patientDto = patientService.signup(signupRequest);

      assertNotNull(patientDto);
      assertEquals("testUsername", patientDto.getUsername());

    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    when(userValidationService.isUsernameUsed("testUsername")).thenReturn(true);

    CustomException exception = assertThrows(CustomException.class,
        () -> patientService.signup(signupRequest));

    assertEquals("이미 사용중인 아이디 입니다.", exception.getErrorMessage());

  }

  @Test
  void failSignup_TwoPasswordDoNotMatch() {

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword2")).thenReturn(false);

      signupRequest.setCheckingPassword("testPassword2");

      CustomException exception = assertThrows(CustomException.class,
          () -> patientService.signup(signupRequest));

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
          () -> patientService.signup(signupRequest));

      assertEquals("올바르지 않은 비밀번호입니다.", exception.getErrorMessage());

    }

  }

  @Test
  void successGetInfo() {

    String username = "testUsername";

    when(patientRepository.findByUsername(username)).thenReturn(patientEntity);
    when(patientMapper.toDto(patientEntity)).thenReturn(patientDto);

    PatientDto result = patientService.getInfo(username);

    assertEquals(patientDto, result);
    verify(patientRepository, times(1)).findByUsername(username);
    verify(patientMapper, times(1)).toDto(patientEntity);

  }

  @Test
  void successUpdateInfo() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(patientEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.encode("newPassword1234")).thenReturn("encodedNewPassword");
    when(patientRepository.save(any(PatientEntity.class))).thenReturn(patientEntity);
    when(patientMapper.toDto(patientEntity)).thenReturn(patientDto);

    PatientDto result = patientService.updateInfo("testUsername", updateRequest);

    assertNotNull(result);
    assertEquals("testUsername", result.getUsername());
    verify(patientRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(bCryptPasswordEncoder, times(1)).encode("newPassword1234");
    verify(patientRepository, times(1)).save(any(PatientEntity.class));
    verify(patientMapper, times(1)).toDto(patientEntity);

  }

  @Test
  void failUpdateInfo_CurrentPasswordDoesNotMatch() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(patientEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(false);

    CustomException exception = assertThrows(CustomException.class, () ->
        patientService.updateInfo("testUsername", updateRequest));

    assertEquals(CURRENT_PASSWORD_DOES_NOT_MATCH.getDescription(), exception.getErrorMessage());
    verify(patientRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(patientRepository, times(0)).save(any(PatientEntity.class));

  }

  @Test
  void failUpdateInfo_PasswordIsRequiredToUpdateInfo() {

    updateRequest.setCurrentPassword(null);

    CustomException exception = assertThrows(CustomException.class, () ->
        patientService.updateInfo("testUsername", updateRequest));

    assertEquals(PASSWORD_IS_REQUIRED_TO_UPDATE_INFO.getDescription(), exception.getErrorMessage());
    verify(patientRepository, times(0)).findByUsername(anyString());
    verify(patientRepository, times(0)).save(any(PatientEntity.class));
  }

  @Test
  void failUpdateInfo_NewPasswordMustBeDifferentFromCurrentOne() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(patientEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.matches("newPassword1234", "encodedTestPassword")).thenReturn(true);


    CustomException exception = assertThrows(CustomException.class, () ->
        patientService.updateInfo("testUsername", updateRequest));

    assertEquals(NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE.getDescription(), exception.getErrorMessage());
    verify(patientRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(bCryptPasswordEncoder, times(1)).matches("newPassword1234", "encodedTestPassword");
    verify(patientRepository, times(0)).save(any(PatientEntity.class));
  }

  @Test
  void failUpdateInfo_InvalidNewPassword() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(patientEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.matches("newPassword1234", "encodedTestPassword")).thenReturn(false);

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(
        PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword("newPassword1234"))
          .thenReturn(false);

      CustomException exception = assertThrows(CustomException.class, () ->
          patientService.updateInfo("testUsername", updateRequest));

      assertEquals(INVALID_PASSWORD.getDescription(), exception.getErrorMessage());
      verify(patientRepository, times(1)).findByUsername("testUsername");
      verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
      verify(bCryptPasswordEncoder, times(1)).matches("newPassword1234", "encodedTestPassword");
      verify(patientRepository, times(0)).save(any(PatientEntity.class));

    }

  }


}