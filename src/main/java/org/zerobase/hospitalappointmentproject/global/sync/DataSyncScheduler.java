package org.zerobase.hospitalappointmentproject.global.sync;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSyncScheduler {

  private final DataSyncService dataSyncService;

  @Scheduled(cron = "0 0 * * * ?")
  public void scheduleForSynchronization() {
    dataSyncService.syncHospitals();
  }

}
