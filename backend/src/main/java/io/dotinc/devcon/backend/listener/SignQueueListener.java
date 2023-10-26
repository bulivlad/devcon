package io.dotinc.devcon.backend.listener;

import io.dotinc.devcon.backend.dto.CustomerDto;
import io.dotinc.devcon.backend.dto.SignDto;
import io.dotinc.devcon.backend.exception.CustomerExistException;
import io.dotinc.devcon.backend.model.Customer;
import io.dotinc.devcon.backend.service.CustomerService;
import io.dotinc.devcon.backend.service.RegistrationService;
import io.dotinc.devcon.backend.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author vlabulim1 on 25.10.2023.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SignQueueListener {
    private final SignService signService;

    @RabbitListener(queues = {"sign-documents"})
    public void onSignMessage(SignDto signDto) {
        log.info("Sign Event Received: {}", signDto);

        signService.sendAck(signDto.getSsn());
    }

}