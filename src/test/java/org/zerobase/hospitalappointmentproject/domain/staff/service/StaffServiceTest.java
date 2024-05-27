package org.zerobase.hospitalappointmentproject.domain.staff.service;

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
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.TWO_PASSWORDS_DO_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.USERNAME_ALREADY_IN_USE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffDto;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffSignup;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.mapper.CustomStaffMapper;
import org.zerobase.hospitalappointmentproject.domain.staff.mapper.StaffMapper;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {
  @Mock
  private StaffRepository staffRepository;
  @Mock
  private StaffMapper staffMapper;
  @Mock
  private CustomStaffMapper customStaffMapper;
  @Mock
  private UserValidationService userValidationService;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private StaffService staffService;

  private StaffSignup.Request signupRequest;
  private StaffInfoUpdate.Request updateRequest;
  private StaffEntity staffEntity;
  private StaffDto staffDto;

  @BeforeEach
  void init() {

    signupRequest = new StaffSignup.Request();
    signupRequest.setUsername("testUsername");
    signupRequest.setPassword("testPassword");
    signupRequest.setCheckingPassword("testPassword");

    updateRequest = new StaffInfoUpdate.Request();
    updateRequest.setCurrentPassword("testPassword");
    updateRequest.setNewPassword("newPassword1234");
    updateRequest.setName("testName");
    updateRequest.setPhoneNumber("123456789");
    updateRequest.setEmail("test@example.com");

    staffEntity = StaffEntity.builder()
        .username("testUsername")
        .password("encodedTestPassword")
        .build();

    staffDto = new StaffDto();
    staffDto.setUsername("testUsername");

  }

  @Test
  void successSignup() {

    try(MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {

      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword")).thenReturn(true);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword("testPassword")).thenReturn(true);
      when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("encodedTestPassword");
      when(staffRepository.save(any(StaffEntity.class))).thenReturn(staffEntity);
      when(staffMapper.toDto(staffEntity)).thenReturn(staffDto);

      StaffDto staffDto = staffService.signup(signupRequest);

      assertNotNull(staffDto);
      assertEquals("testUsername", staffDto.getUsername());

    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    when(userValidationService.isUsernameUsed("testUsername")).thenReturn(true);

    CustomException exception = assertThrows(CustomException.class,
        () -> staffService.signup(signupRequest));

    assertEquals(USERNAME_ALREADY_IN_USE.getDescription(), exception.getErrorMessage());

  }

  @Test
  void failSignup_TwoPasswordDoNotMatch() {

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {

      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword2")).thenReturn(false);

      signupRequest.setCheckingPassword("testPassword2");

      CustomException exception = assertThrows(CustomException.class,
          () -> staffService.signup(signupRequest));

      assertEquals(TWO_PASSWORDS_DO_NOT_MATCH.getDescription(), exception.getErrorMessage());

    }

  }

  @Test
  void failSignup_InvalidPassword() {

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {

      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword")).thenReturn(true);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword("testPassword")).thenReturn(false);

      CustomException exception = assertThrows(CustomException.class,
          () -> staffService.signup(signupRequest));

      assertEquals(INVALID_PASSWORD.getDescription(), exception.getErrorMessage());

    }

  }

  @Test
  void successGetInfo() {

    String username = "testUsername";

    when(staffRepository.findByUsername(username)).thenReturn(staffEntity);
    when(customStaffMapper.toDto(staffEntity)).thenReturn(staffDto);

    StaffDto dto = staffService.getInfo(username);

    assertEquals(staffDto, dto);
    verify(staffRepository, times(1)).findByUsername(username);
    verify(customStaffMapper, times(1)).toDto(staffEntity);

  }

  @Test
  void successUpdateInfo() {

    when(staffRepository.findByUsername("testUsername")).thenReturn(staffEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.encode("newPassword1234")).thenReturn("encodedNewPassword");
    when(staffRepository.save(any(StaffEntity.class))).thenReturn(staffEntity);
    when(staffMapper.toDto(staffEntity)).thenReturn(staffDto);

    StaffDto result = staffService.updateInfo("testUsername", updateRequest);

    assertNotNull(result);
    assertEquals("testUsername", result.getUsername());
    verify(staffRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(bCryptPasswordEncoder, times(1)).encode("newPassword1234");
    verify(staffRepository, times(1)).save(any(StaffEntity.class));
    verify(staffMapper, times(1)).toDto(staffEntity);

  }

  @Test
  void failUpdateInfo_CurrentPasswordDoesNotMatch() {

    when(staffRepository.findByUsername("testUsername")).thenReturn(staffEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(false);

    CustomException exception = assertThrows(CustomException.class, () -> staffService.updateInfo("testUsername", updateRequest));

    assertEquals(CURRENT_PASSWORD_DOES_NOT_MATCH.getDescription(), exception.getErrorMessage());
    verify(staffRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(staffRepository, times(0)).save(any(StaffEntity.class));

  }

  @Test
  void failUpdateInfo_PasswordIsRequiredToUpdateInfo() {

    updateRequest.setCurrentPassword(null);

    CustomException exception = assertThrows(CustomException.class, () ->
        staffService.updateInfo("testUsername", updateRequest));

    assertEquals(PASSWORD_IS_REQUIRED_TO_UPDATE_INFO.getDescription(), exception.getErrorMessage());
    verify(staffRepository, times(0)).findByUsername(anyString());
    verify(staffRepository, times(0)).save(any(StaffEntity.class));
  }

  @Test
  void failUpdateInfo_NewPasswordMustBeDifferentFromCurrentOne() {

    when(staffRepository.findByUsername("testUsername")).thenReturn(staffEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.matches("newPassword1234", "encodedTestPassword")).thenReturn(true);


    CustomException exception = assertThrows(CustomException.class, () ->
        staffService.updateInfo("testUsername", updateRequest));

    assertEquals(NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_ONE.getDescription(), exception.getErrorMessage());
    verify(staffRepository, times(1)).findByUsername("testUsername");
    verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
    verify(bCryptPasswordEncoder, times(1)).matches("newPassword1234", "encodedTestPassword");
    verify(staffRepository, times(0)).save(any(StaffEntity.class));
  }

  @Test
  void failUpdateInfo_InvalidNewPassword() {

    when(staffRepository.findByUsername("testUsername")).thenReturn(staffEntity);
    when(bCryptPasswordEncoder.matches("testPassword", "encodedTestPassword")).thenReturn(true);
    when(bCryptPasswordEncoder.matches("newPassword1234", "encodedTestPassword")).thenReturn(false);

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(
        PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword("newPassword1234"))
          .thenReturn(false);

      CustomException exception = assertThrows(CustomException.class, () ->
          staffService.updateInfo("testUsername", updateRequest));

      assertEquals(INVALID_PASSWORD.getDescription(), exception.getErrorMessage());
      verify(staffRepository, times(1)).findByUsername("testUsername");
      verify(bCryptPasswordEncoder, times(1)).matches("testPassword", "encodedTestPassword");
      verify(bCryptPasswordEncoder, times(1)).matches("newPassword1234", "encodedTestPassword");
      verify(staffRepository, times(0)).save(any(StaffEntity.class));

    }

  }

}