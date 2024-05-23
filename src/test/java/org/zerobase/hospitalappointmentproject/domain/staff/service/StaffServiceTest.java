package org.zerobase.hospitalappointmentproject.domain.staff.service;

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
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffDto;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffSignup;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
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
  private UserValidationService userValidationService;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private StaffService staffService;

  private StaffSignup.Request request;
  private StaffEntity staffEntity;
  private StaffDto staffDto;

  @BeforeEach
  void init() {

    request = new StaffSignup.Request();
    request.setUsername("testUsername");
    request.setPassword("testPassword");
    request.setCheckingPassword("testPassword");

    staffEntity = StaffEntity.builder()
        .username("testUsername")
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
      when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("encodedPassword");
      when(staffRepository.save(any(StaffEntity.class))).thenReturn(staffEntity);
      when(staffMapper.toDto(staffEntity)).thenReturn(staffDto);

      StaffDto staffDto = staffService.signup(request);

      assertNotNull(staffDto);
      assertEquals("testUsername", staffDto.getUsername());

    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    when(userValidationService.isUsernameUsed("testUsername")).thenReturn(true);

    CustomException exception = assertThrows(CustomException.class,
        () -> staffService.signup(request));

    assertEquals("이미 사용중인 아이디 입니다.", exception.getErrorMessage());

  }

  @Test
  void failSignup_TwoPasswordDoNotMatch() {

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword2")).thenReturn(false);

      request.setCheckingPassword("testPassword2");

      CustomException exception = assertThrows(CustomException.class,
          () -> staffService.signup(request));

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
          () -> staffService.signup(request));

      assertEquals("올바르지 않은 비밀번호입니다.", exception.getErrorMessage());
    }
  }

}