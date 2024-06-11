package org.zerobase.hospitalappointmentproject.global.sms;

import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

  private final DefaultMessageService messageService;

  public SmsService(@Value("${sprint.coolsms.api.key}") String apiKey,
                    @Value("${sprint.coolsms.api.secret}") String apiSecret,
                    @Value("${sprint.coolsms.api.domain}") String apiDomain
  ) {
    this.messageService = new DefaultMessageService(apiKey, apiSecret, apiDomain);
  }

}
