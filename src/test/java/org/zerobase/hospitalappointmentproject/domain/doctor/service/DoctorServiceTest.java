package org.zerobase.hospitalappointmentproject.domain.doctor.service;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorSignup;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.doctor.mapper.DoctorMapper;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.specialty.entity.SpecialtyEntity;
import org.zerobase.hospitalappointmentproject.domain.specialty.repository.SpecialtyRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

  @Mock
  private DoctorRepository doctorRepository;
  @Mock
  private DoctorMapper doctorMapper;
  @Mock
  private UserValidationService userValidationService;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Mock
  private SpecialtyRepository specialtyRepository;
  @Mock
  private HospitalRepository hospitalRepository;

  @InjectMocks
  private DoctorService doctorService;

  private DoctorSignup.Request signupRequest;
  private DoctorInfoUpdate.Request updateRequest;
  private DoctorEntity doctorEntity;
  private DoctorDto doctorDto;
  private SpecialtyEntity specialtyEntity;
  private HospitalEntity hospitalEntity;

  @BeforeEach
  void init() {

    signupRequest = new DoctorSignup.Request();
    signupRequest.setUsername("testUsername");
    signupRequest.setPassword("testPassword");
    signupRequest.setCheckingPassword("testPassword");
    signupRequest.setSpecialty("testSpecialty");
    signupRequest.setHospital("testHospital");

    updateRequest = new DoctorInfoUpdate.Request();
    updateRequest.setCurrentPassword("testPassword");
    updateRequest.setNewPassword("newPassword1234");
    updateRequest.setName("testName");
    updateRequest.setPhoneNumber("123456789");
    updateRequest.setEmail("test@example.com");

    specialtyEntity = SpecialtyEntity.builder()
        .name("testSpecialty")
        .build();

    hospitalEntity = HospitalEntity.builder()
        .name("testHospital")
        .build();

    doctorEntity = DoctorEntity.builder()
        .name("testUsername")
        .password("encodedTestPassword")
        .specialty(specialtyEntity)
        .hospital(hospitalEntity)
        .build();

    doctorDto = new DoctorDto();
    doctorDto.setUsername("testUsername");

  }

  @Test
  void successSignup() {

    try(MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {

      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword")).thenReturn(true);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword("testPassword")).thenReturn(true);
      when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("encodedPassword");
      when(doctorRepository.save(any(DoctorEntity.class))).thenReturn(doctorEntity);
      when(doctorMapper.toDto(doctorEntity)).thenReturn(doctorDto);
      when(specialtyRepository.findByName("testSpecialty")).thenReturn(specialtyEntity);
      when(hospitalRepository.findByName("testHospital")).thenReturn(hospitalEntity);

      DoctorDto doctorDto = doctorService.signup(signupRequest);

      assertNotNull(doctorDto);
      assertEquals("testUsername", doctorDto.getUsername());

    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    when(userValidationService.isUsernameUsed("testUsername")).thenReturn(true);

    CustomException exception = assertThrows(CustomException.class,
        () -> doctorService.signup(signupRequest));

    assertEquals("이미 사용중인 아이디 입니다.", exception.getErrorMessage());

  }

  @Test
  void failSignup_TwoPasswordDoNotMatch() {

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword2")).thenReturn(false);

      signupRequest.setCheckingPassword("testPassword2");

      CustomException exception = assertThrows(CustomException.class,
          () -> doctorService.signup(signupRequest));

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
          () -> doctorService.signup(signupRequest));

      assertEquals("올바르지 않은 비밀번호입니다.", exception.getErrorMessage());
    }
  }

  @Test
  void successGetInfo() {

    String username = "testUsername";

    when(doctorRepository.findByUsername(username)).thenReturn(doctorEntity);
    when(doctorMapper.toDto(doctorEntity)).thenReturn(doctorDto);

    DoctorDto dto = doctorService.getInfo(username);

    assertEquals(doctorDto, dto);
    verify(doctorRepository, times(1)).findByUsername(username);
    verify(doctorMapper, times(1)).toDto(doctorEntity);

  }

  @Test
  void successUpdateInfo() {

    when(doctorRepository.findByUsername("testUsername")).thenReturn(doctorEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.encode("newPassword1234")).thenReturn("encodedNewPassword");
    when(doctorRepository.save(any(DoctorEntity.class))).thenReturn(doctorEntity);
    when(doctorMapper.toDto(doctorEntity)).thenReturn(doctorDto);

    DoctorDto dto = doctorService.updateInfo("testUsername", updateRequest);

    assertNotNull(dto);
    assertEquals("testUsername", dto.getUsername());
    verify(doctorRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(bCryptPasswordEncoder, times(1)).encode("newPassword1234");
    verify(doctorRepository, times(1)).save(any(DoctorEntity.class));
    verify(doctorMapper, times(1)).toDto(doctorEntity);

  }

  @Test
  void failUpdateInfo_CurrentPasswordDoesNotMatch() {

    when(doctorRepository.findByUsername("testUsername")).thenReturn(doctorEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(false);

    CustomException exception = assertThrows(CustomException.class, () ->
        doctorService.updateInfo("testUsername", updateRequest));

    assertEquals(CURRENT_PASSWORD_DOES_NOT_MATCH.getDescription(), exception.getErrorMessage());
    verify(doctorRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(doctorRepository, times(0)).save(any(DoctorEntity.class));

  }

  @Test
  void failUpdateInfo_PasswordIsRequiredToUpdateInfo() {

    updateRequest.setCurrentPassword(null);

    CustomException exception = assertThrows(CustomException.class, () ->
        doctorService.updateInfo("testUsername", updateRequest));

    assertEquals(PASSWORD_IS_REQUIRED_TO_UPDATE_INFO.getDescription(), exception.getErrorMessage());
    verify(doctorRepository, times(0)).findByUsername(anyString());
    verify(doctorRepository, times(0)).save(any(DoctorEntity.class));

  }

  @Test
  void failUpdateInfo_NewPasswordMustBeDifferentFromCurrentOne() {

    when(doctorRepository.findByUsername("testUsername")).thenReturn(doctorEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.matches("newPassword1234", "encodedTestPassword")).thenReturn(true);


    CustomException exception = assertThrows(CustomException.class, () ->
        doctorService.updateInfo("testUsername", updateRequest));

    assertEquals(NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE.getDescription(), exception.getErrorMessage());
    verify(doctorRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(bCryptPasswordEncoder, times(1)).matches("newPassword1234", "encodedTestPassword");
    verify(doctorRepository, times(0)).save(any(DoctorEntity.class));

  }

  @Test
  void failUpdateInfo_InvalidNewPassword() {

    when(doctorRepository.findByUsername("testUsername")).thenReturn(doctorEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.matches("newPassword1234", "encodedTestPassword")).thenReturn(false);

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(
        PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword("newPassword1234"))
          .thenReturn(false);

      CustomException exception = assertThrows(CustomException.class, () ->
          doctorService.updateInfo("testUsername", updateRequest));

      assertEquals(INVALID_PASSWORD.getDescription(), exception.getErrorMessage());
      verify(doctorRepository, times(1)).findByUsername("testUsername");
      verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
      verify(bCryptPasswordEncoder, times(1)).matches("newPassword1234", "encodedTestPassword");
      verify(doctorRepository, times(0)).save(any(DoctorEntity.class));

    }

  }

}
