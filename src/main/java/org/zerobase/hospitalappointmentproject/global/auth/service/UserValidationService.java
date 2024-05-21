package org.zerobase.hospitalappointmentproject.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.domain.doctor.repository.DoctorRepository;
import org.zerobase.hospitalappointmentproject.domain.patient.repository.PatientRepository;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;

@Service
@RequiredArgsConstructor
public class UserValidationService {

  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final StaffRepository staffRepository;

  public boolean isUsernameUsed(String username) {

    boolean patientExists = patientRepository.findByUsername(username) != null;
    boolean doctorExists = doctorRepository.findByUsername(username) != null;
    boolean staffExists = staffRepository.findByUsername(username) != null;

    return patientExists || doctorExists || staffExists;

  }

}
