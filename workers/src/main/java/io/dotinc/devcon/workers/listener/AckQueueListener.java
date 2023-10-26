package io.dotinc.devcon.workers.listener;

import io.camunda.zeebe.client.ZeebeClient;
import io.dotinc.devcon.workers.dto.process.CustomerDto;
import io.dotinc.devcon.workers.dto.queue.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author vlabulim1 on 24.10.2023.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AckQueueListener {
    private final ZeebeClient zeebeClient;

    @RabbitListener(queues = {"ack", "sign-ack", "mail-ack"})
    public void onAckMessage(MessageRequestDto messageRequestDto) {
        log.info("Ack Event Received: {}", messageRequestDto);

        zeebeClient.newPublishMessageCommand()
                .messageName(messageRequestDto.getMessageName())
                .correlationKey(messageRequestDto.getCorrelationKey())
                .variables(messageRequestDto.getVariables() != null ? messageRequestDto.getVariables() : new HashMap<>())
                .send()
                .join();
    }
}
