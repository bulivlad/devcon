package io.dotinc.devcon.workers.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.impl.ZeebeObjectMapper;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.dotinc.devcon.workers.dto.process.CustomerDto;
import io.dotinc.devcon.workers.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author vlabulim1 on 01.11.2023.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OnboardingWorker {

    private final SseService sseService;
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

    @JobWorker(type="onboardingStep", autoComplete = false)
    public void onboardingStep(ActivatedJob activatedJob,
                               @Variable Map<String, Object> form,
                               @Variable String processId) {
        String formData = form != null ? zeebeObjectMapper.toJson(form) : "{}";
        Map<String, Object> event = Map.of("form", formData,
                "type", "task",
                "processId", processId,
                "taskId", activatedJob.getElementInstanceKey());

        sseService.emit(event);
        log.info("event sent");

        zeebeClient.newCompleteCommand(activatedJob).send().join();
    }

}
