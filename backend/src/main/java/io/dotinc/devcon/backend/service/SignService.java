package io.dotinc.devcon.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author vlabulim1 on 25.10.2023.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final RabbitTemplate rabbitTemplate;

    public void sendAck(String ssn) {
        log.info("Sending signed ack message with ssn '{}'", ssn);
        rabbitTemplate.convertAndSend("devcon", "sign-ack",
                Map.of("messageName", "SignAck",
                        "correlationKey", ssn));
    }
}
