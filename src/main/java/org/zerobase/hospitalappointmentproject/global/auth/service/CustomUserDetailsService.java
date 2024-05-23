package org.zerobase.hospitalappointmentproject.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.auth.dto.CustomUserDetails;
import org.zerobase.hospitalappointmentproject.global.auth.entity.UserEntity;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final StaffRepository staffRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserEntity userdata;

    userdata = patientRepository.findByUsername(username);
    if (userdata != null) {
      return new CustomUserDetails(userdata);
    }

    userdata = doctorRepository.findByUsername(username);
    if (userdata != null) {
      return new CustomUserDetails(userdata);
    }

    userdata = staffRepository.findByUsername(username);
    if (userdata != null) {
      return new CustomUserDetails(userdata);
    }

    return null;
  }

}
