package org.zerobase.hospitalappointmentproject.domain.patient.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
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
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

  @Mock
  private PatientRepository patientRepository;
  @Mock
  private UserValidationService userValidationService;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private PatientService patientService;

  @Test
  void successSignup() {

    PatientSignup.Request request = new PatientSignup.Request();
    request.setUsername("testUsername");
    request.setPassword("password123");
    request.setCheckingPassword("password123");
    request.setGender("F");
    request.setBirthYear(2024);
    request.setBirthMonth(1);
    request.setBirthDay(1);

    when(userValidationService.isUsernameUsed(request.getUsername())).thenReturn(false);
    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())).thenReturn(true);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword(request.getPassword())).thenReturn(true);
      when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

      PatientEntity patient = PatientEntity.builder().username("testUsername").password("encodedPassword").build();
      when(patientRepository.save(any(PatientEntity.class))).thenReturn(patient);

      PatientDto patientDto = patientService.signup(request);

      assertNotNull(patientDto);
      verify(patientRepository, times(1)).save(any(PatientEntity.class));

    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    PatientSignup.Request request = new PatientSignup.Request();
    request.setUsername("testUsername");

    when(userValidationService.isUsernameUsed(request.getUsername())).thenReturn(true);

    assertThrows(CustomException.class, () -> patientService.signup(request));

  }

  @Test
  void failSignup_PasswordDoNotMatch() {

    PatientSignup.Request request = new PatientSignup.Request();
    request.setPassword("password123");
    request.setCheckingPassword("differentPassword");

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())).thenReturn(false);

      assertThrows(CustomException.class, () -> patientService.signup(request));
    }

  }

  @Test
  void successGetInfo() {

    PatientEntity patient = PatientEntity.builder().username("testUsername").build();
    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.of(patient));

    PatientDto patientDto = patientService.getInfo("testUsername");

    assertNotNull(patientDto);

  }

  @Test
  void failGetInfo_PatientNotFound() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> patientService.getInfo("testUsername"));

  }

  @Test
  void successUpdateInfo() {

    PatientEntity patient = PatientEntity.builder().password("encodedPassword")
        .birthDate(LocalDate.of(2024, 1, 1)).build();

    PatientInfoUpdate.Request request = new PatientInfoUpdate.Request();
    request.setCurrentPassword("password123");
    request.setNewPassword("newPassword123");

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.of(patient));
    when(bCryptPasswordEncoder.matches(request.getCurrentPassword(), patient.getPassword())).thenReturn(true);

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword(request.getNewPassword())).thenReturn(true);
      when(bCryptPasswordEncoder.encode(request.getNewPassword())).thenReturn("newEncodedPassword");

      PatientEntity updatedPatient = PatientEntity.builder()
          .username("testUsername")
          .password("newEncodedPassword")
          .build();

      when(patientRepository.save(any(PatientEntity.class))).thenReturn(updatedPatient);

      PatientDto patientDto = patientService.updateInfo("testUsername", request);

      assertNotNull(patientDto);
      verify(patientRepository, times(1)).save(any(PatientEntity.class));
    }

  }

  @Test
  void failUpdateInfo_CurrentPasswordDoesNotMatch() {

    PatientEntity patient = PatientEntity.builder().password("encodedPassword").build();

    PatientInfoUpdate.Request request = new PatientInfoUpdate.Request();
    request.setCurrentPassword("wrongPassword");

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.of(patient));
    when(bCryptPasswordEncoder.matches(request.getCurrentPassword(), patient.getPassword())).thenReturn(false);

    assertThrows(CustomException.class, () -> patientService.updateInfo("testUsername", request));

  }
  @Test
  void successDeleteInfo() {

    PatientEntity patient = PatientEntity.builder().password("encodedPassword").build();

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.of(patient));
    when(bCryptPasswordEncoder.matches("password123", patient.getPassword())).thenReturn(true);

    patientService.deleteInfo("testUsername", "password123");

    verify(patientRepository, times(1)).delete(patient);

  }

  @Test
  void failDeleteInfo_PatientNotFound() {

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> patientService.deleteInfo("testUsername", "password123"));

  }

  @Test
  void failDeleteInfo_PasswordDoesNotMatch() {

    PatientEntity patient = PatientEntity.builder().password("encodedPassword").build();

    when(patientRepository.findByUsername("testUsername")).thenReturn(Optional.of(patient));
    when(bCryptPasswordEncoder.matches("wrongPassword", patient.getPassword())).thenReturn(false);

    assertThrows(CustomException.class, () -> patientService.deleteInfo("testUsername", "wrongPassword"));

  }

}