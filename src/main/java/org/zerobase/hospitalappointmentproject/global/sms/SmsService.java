package org.zerobase.hospitalappointmentproject.global.sms;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.FAILED_TO_SEND_MESSAGE;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Service
public class SmsService {

  private final DefaultMessageService messageService;

  @Value("${spring.coolsms.from.phone-number}")
  private String phoneNumber;

  public SmsService(@Value("${spring.coolsms.api.key}") String apiKey,
                    @Value("${spring.coolsms.api.secret}") String apiSecret,
                    @Value("${spring.coolsms.api.domain}") String apiDomain
  ) {
    this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, apiDomain);
  }

  public void sendSms(String to, String text) throws CustomException {

    Message message = new Message();
    message.setFrom(phoneNumber);
    message.setTo(to);
    message.setText(text);

    SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
    SingleMessageSentResponse response = this.messageService.sendOne(request);

    if (response != null && !response.getStatusCode().equals("200")) {
      throw new CustomException(FAILED_TO_SEND_MESSAGE);
    }

  }

}
