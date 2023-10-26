package io.dotinc.devcon.workers.controller;

import io.camunda.tasklist.exception.TaskListException;
import io.dotinc.devcon.workers.dto.request.StartWorkflowDto;
import io.dotinc.devcon.workers.dto.response.TaskResponse;
import io.dotinc.devcon.workers.service.TaskService;
import io.dotinc.devcon.workers.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vlabulim1 on 22.10.2023.
 */

@RestController
@RequestMapping("/workflow")
@RequiredArgsConstructor
public class WorkflowController {
    private final WorkflowService workflowService;
    private final TaskService taskService;

    @PostMapping("/start")
    public Long startWorkflow(@RequestBody StartWorkflowDto startWorkflowDto) {
        return workflowService.startProcess(startWorkflowDto);
    }

    @GetMapping("/{process}/get-task")
    public TaskResponse getTask(@PathVariable String process) throws Exception {
        return taskService.getTask(process);
    }
}
