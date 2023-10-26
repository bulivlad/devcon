package io.dotinc.devcon.frontend.service;

import io.dotinc.devcon.frontend.dto.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author vlabulim1 on 22.10.2023.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class BackendService {
    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;

    public String startWorkflow() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:9091/workflow/start",
                Map.of("processId", "Process_onboarding"), String.class);

        if (!response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
            throw new Exception("Cannot start workflow");
        }

        return response.getBody();
    }

    public TaskResponse getTask(String processId) throws Exception {
        return retryTemplate.execute(ctx -> {
            ResponseEntity<TaskResponse> response = restTemplate.getForEntity("http://localhost:9091/workflow/" + processId + "/get-task", TaskResponse.class);

            if (!response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
                throw new Exception("Cannot start workflow");
            }

            log.info(response.getBody().toString());
            return response.getBody();
        });
    }
}
