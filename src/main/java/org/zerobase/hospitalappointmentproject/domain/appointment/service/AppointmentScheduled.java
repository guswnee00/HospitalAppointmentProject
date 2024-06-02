package org.zerobase.hospitalappointmentproject.domain.appointment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentScheduled {

  private final AppointmentService appointmentService;

  @Scheduled(cron = "0 10,25,40,55 * * * ?")
  public void scheduleForCompleteConsultation() {
    appointmentService.completeConsultation();
  }

  @Scheduled(cron = "0 0 9 * * ?")
  public void scheduleForConfirmAppointmentForAllHospital() {
    appointmentService.confirmAppointmentsForAllHospitals();
  }

}
