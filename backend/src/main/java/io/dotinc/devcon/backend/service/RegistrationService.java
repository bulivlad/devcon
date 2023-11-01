package io.dotinc.devcon.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author vlabulim1 on 23.10.2023.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RabbitTemplate rabbitTemplate;

    public void sendAck(String ssn, String status) {
        log.info("Sending ack message with ssn '{}' status '{}'", ssn, status);
        rabbitTemplate.convertAndSend("devcon", "ack",
                Map.of("messageName", "CustomerSave-" + status,
                        "correlationKey", ssn));
    }
}
