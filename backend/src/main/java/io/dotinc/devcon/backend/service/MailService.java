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
public class MailService {
    private final RabbitTemplate rabbitTemplate;

    public void sendCustomerMailAck(String ssn, String status) {
        log.info("Sending ack message with ssn '{}' status '{}'", ssn, status);
        rabbitTemplate.convertAndSend("devcon", "mail-ack",
                Map.of("messageName", "CustomerMail-" + status,
                        "correlationKey", ssn));
    }

    public void sendInternalMailAck(String mail, String status) {
        log.info("Sending ack message with mail '{}' status '{}'", mail, status);
        rabbitTemplate.convertAndSend("internal", "mail-ack",
                Map.of("messageName", "InternalMail-" + status,
                        "correlationKey", mail));
    }
}
