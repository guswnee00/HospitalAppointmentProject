package org.zerobase.hospitalappointmentproject.domain.staff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.auth.service.UserValidationService;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;
import org.zerobase.hospitalappointmentproject.global.util.PasswordUtils;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {
  @Mock
  private StaffRepository staffRepository;
  @Mock
  private UserValidationService userValidationService;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private StaffService staffService;

  @Test
  void successSignup() {

    StaffSignup.Request request = new StaffSignup.Request();
    request.setUsername("testUsername");
    request.setPassword("password123");
    request.setCheckingPassword("password123");

    when(userValidationService.isUsernameUsed(request.getUsername())).thenReturn(false);
    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())).thenReturn(true);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword(request.getPassword())).thenReturn(true);
      when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

      StaffEntity staff = StaffEntity.builder().username("testUsername").password("encodedPassword").build();
      when(staffRepository.save(any(StaffEntity.class))).thenReturn(staff);

      StaffDto staffDto = staffService.signup(request);

      assertNotNull(staffDto);
      verify(staffRepository, times(1)).save(any(StaffEntity.class));
    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    StaffSignup.Request request = new StaffSignup.Request();
    request.setUsername("testUsername");

    when(userValidationService.isUsernameUsed(request.getUsername())).thenReturn(true);

    assertThrows(CustomException.class, () -> staffService.signup(request));

  }

  @Test
  void failSignup_PasswordDoNotMatch() {

    StaffSignup.Request request = new StaffSignup.Request();
    request.setPassword("password123");
    request.setCheckingPassword("differentPassword");

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())).thenReturn(false);

      assertThrows(CustomException.class, () -> staffService.signup(request));

    }

  }

  @Test
  void successGetInfo() {

    StaffEntity staff = StaffEntity.builder().username("testUsername").build();
    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));

    StaffDto staffDto = staffService.getInfo("testUsername");

    assertNotNull(staffDto);

  }

  @Test
  void failGetInfo_StaffNotFound() {

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> staffService.getInfo("testUsername"));

  }

  @Test
  void successUpdateInfo() {

    StaffEntity staff = StaffEntity.builder().password("encodedPassword").build();

    StaffInfoUpdate.Request request = new StaffInfoUpdate.Request();
    request.setCurrentPassword("password123");
    request.setNewPassword("newPassword123");

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));
    when(bCryptPasswordEncoder.matches(request.getCurrentPassword(), staff.getPassword())).thenReturn(true);
    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword(request.getNewPassword())).thenReturn(true);
      when(bCryptPasswordEncoder.encode(request.getNewPassword())).thenReturn("newEncodedPassword");

      StaffEntity updatedStaff = StaffEntity.builder()
          .username("testUsername")
          .password("newEncodedPassword")
          .build();

      when(staffRepository.save(any(StaffEntity.class))).thenReturn(updatedStaff);

      StaffDto staffDto = staffService.updateInfo("testUsername", request);

      assertNotNull(staffDto);
      verify(staffRepository, times(1)).save(any(StaffEntity.class));
    }

  }

  @Test
  void failUpdateInfo_StaffNotFound() {

    lenient().when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    StaffInfoUpdate.Request request = new StaffInfoUpdate.Request();

    assertThrows(CustomException.class, () -> staffService.updateInfo("testUsername", request));

  }

  @Test
  void failUpdateInfo_CurrentPasswordDoesNotMatch() {

    StaffEntity staff = StaffEntity.builder().password("encodedPassword").build();

    StaffInfoUpdate.Request request = new StaffInfoUpdate.Request();
    request.setCurrentPassword("wrongPassword");

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));
    when(bCryptPasswordEncoder.matches(request.getCurrentPassword(), staff.getPassword())).thenReturn(false);

    assertThrows(CustomException.class, () -> staffService.updateInfo("testUsername", request));

  }

}