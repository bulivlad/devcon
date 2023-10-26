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
 * @author vlabulim1 on 23.10.2023.
 */

@Component
@RequiredArgsConstructor
public class MappingWorker {
    private final ZeebeObjectMapper zeebeObjectMapper;

    @JobWorker
    public Map<String, CustomerDto> mapVariablesToCustomer(ActivatedJob activatedJob) {
        return Map.of("customer", zeebeObjectMapper.fromJson(activatedJob.getVariables(), CustomerDto.class));
    }
}
