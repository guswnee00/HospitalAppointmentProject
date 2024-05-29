package org.zerobase.hospitalappointmentproject.domain.hospital.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
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
import org.zerobase.hospitalappointmentproject.domain.hospital.mapper.HospitalMapper;
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
  private HospitalMapper hospitalMapper;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private HospitalService hospitalService;

  private StaffEntity staff, staff2;
  private HospitalEntity hospital;
  private HospitalRegister.Request registerRequest;
  private HospitalInfoUpdate.Request updateRequest;

  @BeforeEach
  void init() {

    staff = StaffEntity.builder()
                      .id(1L)
                      .username("testUsername")
                      .hospital(null)
                      .build();

    registerRequest = new HospitalRegister.Request();
    registerRequest.setName("testHospital");

    hospital = HospitalEntity.builder()
                            .id(1L)
                            .name("testHospital")
                            .build();

    staff2 = StaffEntity.builder()
                      .id(2L)
                      .username("testUsername2")
                      .password("encodedTestPassword")
                      .hospital(hospital)
                      .build();

    updateRequest = new HospitalInfoUpdate.Request();
    updateRequest.setPassword("testPassword");
    updateRequest.setDescription("description");

  }

  @Test
  void successRegister() {

    when(staffRepository.findByUsername("testUsername")).thenReturn(staff);
    when(hospitalRepository.existsByName(registerRequest.getName())).thenReturn(false);
    when(hospitalRepository.save(any(HospitalEntity.class))).thenAnswer(i -> i.getArguments()[0]);
    when(hospitalMapper.toDto(any(HospitalEntity.class))).thenReturn(new HospitalDto());

    HospitalDto hospitalDto = hospitalService.register("testUsername", registerRequest);

    verify(staffRepository).save(any(StaffEntity.class));
    verify(hospitalRepository).save(any(HospitalEntity.class));
    assertNotNull(hospitalDto);

  }

  @Test
  void failRegister_HospitalIsAlreadyRegistered() {

    when(staffRepository.findByUsername("testUsername")).thenReturn(staff);
    staff = staff.toBuilder().hospital(new HospitalEntity()).build();
    when(staffRepository.findByUsername("testUsername")).thenReturn(staff);

    assertThrows(CustomException.class,
        () -> hospitalService.register("testUsername", registerRequest));

  }

  @Test
  void failRegister_ThisHospitalNameAlreadyExists() {

    when(staffRepository.findByUsername("testUsername")).thenReturn(staff);
    when(hospitalRepository.existsByName(registerRequest.getName())).thenReturn(true);

    assertThrows(CustomException.class,
        () -> hospitalService.register("testUsername", registerRequest));

  }

  @Test
  void successUpdateInfo() {

    when(staffRepository.findByUsername("testUsername2")).thenReturn(staff2);
    when(bCryptPasswordEncoder.matches(updateRequest.getPassword(), staff2.getPassword())).thenReturn(true);
    when(hospitalRepository.save(any(HospitalEntity.class))).thenAnswer(i -> i.getArguments()[0]);
    when(hospitalMapper.toDto(any(HospitalEntity.class))).thenReturn(new HospitalDto());

    HospitalDto hospitalDto = hospitalService.updateInfo("testUsername2", updateRequest);

    verify(hospitalRepository).save(any(HospitalEntity.class));
    assertNotNull(hospitalDto);

  }

  @Test
  void failUpdateInfo_PasswordIsRequiredToUpdateInfo() {

    updateRequest.setPassword(null);

    assertThrows(CustomException.class,
        () -> hospitalService.updateInfo("testUsername2", updateRequest));

  }

  @Test
  void failUpdateInfo_CurrentPasswordDoesNotMatch() {

    when(staffRepository.findByUsername("testUsername2")).thenReturn(staff2);
    when(bCryptPasswordEncoder.matches(updateRequest.getPassword(), staff2.getPassword())).thenReturn(false);

    assertThrows(CustomException.class,
        () -> hospitalService.updateInfo("testUsername2", updateRequest));

  }

  @Test
  void successDelete() {

    String password = "encodedTestPassword";
    when(staffRepository.findByUsername("testUsername2")).thenReturn(staff2);
    when(bCryptPasswordEncoder.matches(password, staff2.getPassword())).thenReturn(true);

    hospitalService.delete("testUsername2", password);

    verify(staffRepository).save(any(StaffEntity.class));
    verify(hospitalRepository).delete(any(HospitalEntity.class));

  }

  @Test
  void failDelete_PasswordIsNull() {

    String password = null;
    when(staffRepository.findByUsername("testUsername2")).thenReturn(staff);

    assertThrows(CustomException.class,
        () -> hospitalService.delete("testUsername2", password));

  }

  @Test
  void failDelete_PasswordDoesNotMatch() {

    String password = "anotherTestPassword";
    when(staffRepository.findByUsername("testUsername2")).thenReturn(staff);
    when(bCryptPasswordEncoder.matches(password, staff.getPassword())).thenReturn(false);

    assertThrows(CustomException.class,
        () -> hospitalService.delete("testUsername2", password));

  }

}