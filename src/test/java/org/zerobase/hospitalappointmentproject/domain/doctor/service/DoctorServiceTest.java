package org.zerobase.hospitalappointmentproject.domain.doctor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorSignup;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
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
  private SpecialtyRepository specialtyRepository;
  @Mock
  private HospitalRepository hospitalRepository;
  @Mock
  private UserValidationService userValidationService;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private DoctorService doctorService;

  @Test
  void successSignup() {

    DoctorSignup.Request request = new DoctorSignup.Request();
    request.setUsername("testUsername");
    request.setPassword("password123");
    request.setCheckingPassword("password123");
    request.setSpecialtyName("Cardiology");
    request.setHospitalName("General Hospital");

    when(userValidationService.isUsernameUsed(request.getUsername())).thenReturn(false);
    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())).thenReturn(true);
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword(request.getPassword())).thenReturn(true);
      when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

      SpecialtyEntity specialty = SpecialtyEntity.builder().name("Cardiology").build();
      HospitalEntity hospital = HospitalEntity.builder().name("General Hospital").build();

      when(specialtyRepository.findByName("Cardiology")).thenReturn(Optional.of(specialty));
      when(hospitalRepository.findByName("General Hospital")).thenReturn(Optional.of(hospital));

      DoctorEntity doctor = DoctorEntity.builder()
          .username("testUsername")
          .password("encodedPassword")
          .specialty(specialty)
          .hospital(hospital)
          .build();

      when(doctorRepository.save(any(DoctorEntity.class))).thenReturn(doctor);

      DoctorDto doctorDto = doctorService.signup(request);

      assertNotNull(doctorDto);
      verify(doctorRepository, times(1)).save(any(DoctorEntity.class));

    }

  }

  @Test
  void failSignup_UsernameAlreadyInUse() {

    DoctorSignup.Request request = new DoctorSignup.Request();
    request.setUsername("testUsername");

    when(userValidationService.isUsernameUsed(request.getUsername())).thenReturn(true);

    assertThrows(CustomException.class, () -> doctorService.signup(request));

  }

  @Test
  void failSignup_PasswordDoNotMatch() {

    DoctorSignup.Request request = new DoctorSignup.Request();
    request.setPassword("password123");
    request.setCheckingPassword("differentPassword");

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.equalPassword(request.getPassword(), request.getCheckingPassword())).thenReturn(false);

      assertThrows(CustomException.class, () -> doctorService.signup(request));
    }

  }

  @Test
  void successGetInfo() {

    SpecialtyEntity specialty = SpecialtyEntity.builder()
        .name("testSpecialty")
        .build();

    HospitalEntity hospital = HospitalEntity.builder()
        .name("testHospital")
        .build();

    DoctorEntity doctor = DoctorEntity.builder()
        .username("testUsername")
        .specialty(specialty)
        .hospital(hospital)
        .build();

    when(doctorRepository.findByUsername("testUsername")).thenReturn(Optional.of(doctor));

    DoctorDto doctorDto = doctorService.getInfo("testUsername");

    assertNotNull(doctorDto);
    assertEquals("testUsername", doctorDto.getUsername());
    assertEquals("testSpecialty", doctorDto.getSpecialtyName());
    assertEquals("testHospital", doctorDto.getHospitalName());

  }

  @Test
  void failGetInfo_DoctorNotFound() {

    when(doctorRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> doctorService.getInfo("testUsername"));

  }

  @Test
  void successUpdateInfo() {

    SpecialtyEntity specialty = SpecialtyEntity.builder()
        .name("testSpecialty")
        .build();

    HospitalEntity hospital = HospitalEntity.builder()
        .name("testHospital")
        .build();

    DoctorEntity doctor = DoctorEntity.builder()
        .username("testUsername")
        .password("encodedPassword")
        .specialty(specialty)
        .hospital(hospital)
        .build();

    DoctorInfoUpdate.Request request = new DoctorInfoUpdate.Request();
    request.setCurrentPassword("password123");
    request.setNewPassword("newPassword123");

    when(doctorRepository.findByUsername("testUsername")).thenReturn(Optional.of(doctor));
    when(bCryptPasswordEncoder.matches(request.getCurrentPassword(), doctor.getPassword())).thenReturn(true);

    try (MockedStatic<PasswordUtils> passwordUtilsMockedStatic = Mockito.mockStatic(PasswordUtils.class)) {
      passwordUtilsMockedStatic.when(() -> PasswordUtils.validationPassword(request.getNewPassword())).thenReturn(true);
      when(bCryptPasswordEncoder.encode(request.getNewPassword())).thenReturn("newEncodedPassword");

      DoctorEntity updatedDoctor = doctor.toBuilder().password("newEncodedPassword").build();
      when(doctorRepository.save(any(DoctorEntity.class))).thenReturn(updatedDoctor);

      DoctorDto doctorDto = doctorService.updateInfo("testUsername", request);

      assertNotNull(doctorDto);
      verify(doctorRepository, times(1)).save(any(DoctorEntity.class));
    }

  }

  @Test
  void failUpdateInfo_DoctorNotFound() {

    lenient().when(doctorRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    DoctorInfoUpdate.Request request = new DoctorInfoUpdate.Request();

    assertThrows(CustomException.class, () -> doctorService.updateInfo("testUsername", request));

  }

  @Test
  void failUpdateInfo_PasswordsDoNotMatch() {

    DoctorEntity doctor = DoctorEntity.builder().password("encodedPassword").build();

    DoctorInfoUpdate.Request request = new DoctorInfoUpdate.Request();
    request.setCurrentPassword("wrongPassword");

    when(doctorRepository.findByUsername("testUsername")).thenReturn(Optional.of(doctor));
    when(bCryptPasswordEncoder.matches(request.getCurrentPassword(), doctor.getPassword())).thenReturn(false);

    assertThrows(CustomException.class, () -> doctorService.updateInfo("testUsername", request));

  }

  @Test
  void successDeleteInfo() {

    DoctorEntity doctor = DoctorEntity.builder().password("encodedPassword").build();

    when(doctorRepository.findByUsername("testUsername")).thenReturn(Optional.of(doctor));
    when(bCryptPasswordEncoder.matches("password123", doctor.getPassword())).thenReturn(true);

    doctorService.deleteInfo("testUsername", "password123");

    verify(doctorRepository, times(1)).delete(doctor);

  }

  @Test
  void failDeleteInfo_DoctorNotFound() {

    when(doctorRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> doctorService.deleteInfo("testUsername", "password123"));

  }

  @Test
  void failDeleteInfo_PasswordDoesNotMatch() {

    DoctorEntity doctor = DoctorEntity.builder().password("encodedPassword").build();

    when(doctorRepository.findByUsername("testUsername")).thenReturn(Optional.of(doctor));
    when(bCryptPasswordEncoder.matches("wrongPassword", doctor.getPassword())).thenReturn(false);

    assertThrows(CustomException.class, () -> doctorService.deleteInfo("testUsername", "wrongPassword"));

  }

}
