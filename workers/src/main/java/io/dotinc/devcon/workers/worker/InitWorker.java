package io.dotinc.devcon.workers.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
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
public class InitWorker {
    private final ZeebeClient zeebeClient;

    @JobWorker(autoComplete = false)
    public void initProcess(ActivatedJob activatedJob) {
        zeebeClient.newCompleteCommand(activatedJob)
                .variables(Map.of("processId", String.valueOf(activatedJob.getProcessInstanceKey())))
                .send()
                .join();
    }
}
