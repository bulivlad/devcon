package io.dotinc.devcon.workers.service;

import io.camunda.zeebe.client.ZeebeClient;
import io.dotinc.devcon.workers.dto.request.StartWorkflowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author vlabulim1 on 22.10.2023.
 */

@Service
@RequiredArgsConstructor
public class WorkflowService {
    private final ZeebeClient zeebeClient;

    public Long startProcess(StartWorkflowDto startWorkflowDto) {
        return zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(startWorkflowDto.getProcessId())
                .latestVersion()
                .send()
                .join()
                .getProcessInstanceKey();
    }
}
