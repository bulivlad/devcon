package io.dotinc.devcon.workers.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.impl.ZeebeObjectMapper;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.dotinc.devcon.workers.dto.process.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author vlabulim1 on 24.10.2023.
 */

@Component
@RequiredArgsConstructor
public class RegistrationWorker {
    private final ZeebeClient zeebeClient;
    private final ZeebeObjectMapper zeebeObjectMapper;

    @JobWorker(type = "start-backoffice-process")
    public void startBackofficeProcess(ActivatedJob activatedJob) {
        CustomerDto customer = zeebeObjectMapper.fromJson(activatedJob.getVariables(), CustomerDto.class);
        zeebeClient.newPublishMessageCommand()
                .messageName("Start_VerificationProcess")
                .correlationKey(customer.getSsn())
                .variables(Map.of("ssn", customer.getSsn()))
                .send();
    }
}
