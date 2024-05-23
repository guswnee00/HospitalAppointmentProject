package org.zerobase.hospitalappointmentproject.domain.doctor.service;

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
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;
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

  private DoctorSignup.Request request;
  private DoctorEntity doctorEntity;
  private DoctorDto doctorDto;
  private SpecialtyEntity specialtyEntity;
  private HospitalEntity hospitalEntity;

  @BeforeEach
  void init() {

    request = new DoctorSignup.Request();
    request.setUsername("testUsername");
    request.setPassword("testPassword");
    request.setCheckingPassword("testPassword");
    request.setSpecialty("testSpecialty");
    request.setHospital("testHospital");

    specialtyEntity = SpecialtyEntity.builder()
        .name("testSpecialty")
        .build();

    hospitalEntity = HospitalEntity.builder()
        .name("testHospital")
        .build();

    doctorEntity = DoctorEntity.builder()
        .name("testUsername")
        .password("testPassword")
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

      DoctorDto doctorDto = doctorService.signup(request);

      assertNotNull(doctorDto);
      assertEquals("testUsername", doctorDto.getUsername());

    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    when(userValidationService.isUsernameUsed("testUsername")).thenReturn(true);

    CustomException exception = assertThrows(CustomException.class,
        () -> doctorService.signup(request));

    assertEquals("이미 사용중인 아이디 입니다.", exception.getErrorMessage());

  }

  @Test
  void failSignup_TwoPasswordDoNotMatch() {

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      when(userValidationService.isUsernameUsed("testUsername")).thenReturn(false);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword("testPassword", "testPassword2")).thenReturn(false);

      request.setCheckingPassword("testPassword2");

      CustomException exception = assertThrows(CustomException.class,
          () -> doctorService.signup(request));

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
          () -> doctorService.signup(request));

      assertEquals("올바르지 않은 비밀번호입니다.", exception.getErrorMessage());
    }
  }


}