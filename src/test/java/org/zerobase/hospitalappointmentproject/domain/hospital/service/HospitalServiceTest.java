package org.zerobase.hospitalappointmentproject.domain.hospital.service;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalRegister;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class HospitalServiceTest {

  @Mock
  private HospitalRepository hospitalRepository;
  @Mock
  private StaffRepository staffRepository;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private HospitalService hospitalService;

  @Test
  void successRegister() {

    StaffEntity staff = StaffEntity.builder().username("username").build();
    HospitalRegister.Request request = new HospitalRegister.Request();
    request.setName("testHospital");

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));
    when(hospitalRepository.existsByName(request.getName())).thenReturn(false);

    HospitalEntity hospital = HospitalEntity.builder().name("testHospital").build();
    when(hospitalRepository.save(any(HospitalEntity.class))).thenReturn(hospital);

    staff = staff.toBuilder().hospital(hospital).build();
    when(staffRepository.save(any(StaffEntity.class))).thenReturn(staff);

    HospitalDto hospitalDto = hospitalService.register("testUsername", request);

    assertNotNull(hospitalDto);
    verify(staffRepository, times(1)).save(any(StaffEntity.class));
    verify(hospitalRepository, times(1)).save(any(HospitalEntity.class));
  }

  @Test
  void failRegister_StaffNotFound() {

    HospitalRegister.Request request = new HospitalRegister.Request();

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> hospitalService.register("testUsername", request));

  }

  @Test
  void failRegister_HospitalIsAlreadyRegistered() {

    StaffEntity staff = StaffEntity.builder().username("testUsername").hospital(new HospitalEntity()).build();
    HospitalRegister.Request request = new HospitalRegister.Request();

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));

    assertThrows(CustomException.class, () -> hospitalService.register("testUsername", request));

  }

  @Test
  void failRegister_ThisHospitalNameAlreadyExists() {

    StaffEntity staff = StaffEntity.builder().username("testUsername").build();
    HospitalRegister.Request request = new HospitalRegister.Request();
    request.setName("testHospital");

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));
    when(hospitalRepository.existsByName(request.getName())).thenReturn(true);

    assertThrows(CustomException.class, () -> hospitalService.register("testUsername", request));

  }

  @Test
  void successUpdateInfo() {

    StaffEntity staff = StaffEntity.builder().username("testUsername").password("encodedPassword").build();
    HospitalEntity hospital = HospitalEntity.builder().name("hospital1").build();
    staff = staff.toBuilder().hospital(hospital).build();

    HospitalInfoUpdate.Request request = new HospitalInfoUpdate.Request();
    request.setPassword("password123");

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));
    when(bCryptPasswordEncoder.matches(request.getPassword(), staff.getPassword())).thenReturn(true);

    HospitalEntity updatedHospital = HospitalEntity.builder().name("hospital2").build();
    when(hospitalRepository.save(any(HospitalEntity.class))).thenReturn(updatedHospital);

    HospitalDto hospitalDto = hospitalService.updateInfo("testUsername", request);

    assertNotNull(hospitalDto);
    verify(hospitalRepository, times(1)).save(any(HospitalEntity.class));

  }

  @Test
  void failUpdateInfo_StaffNotFound() {

    HospitalInfoUpdate.Request request = new HospitalInfoUpdate.Request();

    lenient().when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> hospitalService.updateInfo("testUsername", request));

  }

  @Test
  void failUpdateInfo_CurrentPasswordDoesNotMatch() {

    StaffEntity staff = StaffEntity.builder().username("testUsername").password("encodedPassword").build();
    HospitalInfoUpdate.Request request = new HospitalInfoUpdate.Request();
    request.setPassword("wrongPassword");

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));
    when(bCryptPasswordEncoder.matches(request.getPassword(), staff.getPassword())).thenReturn(false);

    assertThrows(CustomException.class, () -> hospitalService.updateInfo("testUsername", request));

  }

  @Test
  void successDelete() {

    StaffEntity staff = StaffEntity.builder().username("testUsername").password("encodedPassword").build();
    HospitalEntity hospital = HospitalEntity.builder().name("hospital1").build();
    staff = staff.toBuilder().hospital(hospital).build();

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));
    when(bCryptPasswordEncoder.matches("password123", staff.getPassword())).thenReturn(true);

    hospitalService.delete("testUsername", "password123");

    verify(staffRepository, times(1)).save(any(StaffEntity.class));
    verify(hospitalRepository, times(1)).delete(hospital);

  }

  @Test
  void failDelete_StaffNotFound() {
    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> hospitalService.delete("testUsername", "password123"));
  }

  @Test
  void failDelete_PasswordDoesNotMatch() {

    StaffEntity staff = StaffEntity.builder().username("testUsername").password("encodedPassword").build();

    when(staffRepository.findByUsername("testUsername")).thenReturn(Optional.of(staff));
    when(bCryptPasswordEncoder.matches("wrongPassword", staff.getPassword())).thenReturn(false);

    assertThrows(CustomException.class, () -> hospitalService.delete("testUsername", "wrongPassword"));

  }

}