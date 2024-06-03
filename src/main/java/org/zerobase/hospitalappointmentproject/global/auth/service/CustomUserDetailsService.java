package org.zerobase.hospitalappointmentproject.global.auth.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.USERNAME_DOES_NOT_EXIST;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.auth.dto.CustomUserDetails;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final StaffRepository staffRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    return patientRepository.findByUsername(username).map(CustomUserDetails::new)
        .or(() -> doctorRepository.findByUsername(username).map(CustomUserDetails::new))
        .or(() -> staffRepository.findByUsername(username).map(CustomUserDetails::new))
        .orElseThrow(() -> new CustomException(USERNAME_DOES_NOT_EXIST));

  }

}
