package io.dotinc.devcon.workers.controller;

import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskState;
import io.dotinc.devcon.workers.dto.response.TaskResponse;
import io.dotinc.devcon.workers.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author vlabulim1 on 23.10.2023.
 */

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/v1/{taskId}/complete")
    public TaskResponse completeUserTask(@PathVariable String taskId,
                                         @RequestBody Map<String, Object> body) throws Exception {
        Task completed = taskService.completeEndUserTask(taskId, body);
        if (completed.getTaskState() != TaskState.COMPLETED) {
            throw new Exception("Cannot complete task " + completed.getId());
        }

        return taskService.getTask(completed.getProcessInstanceId());
    }

    @PostMapping("/{processId}/complete")
    public void sendCompleteMessage(@PathVariable String processId,
                                         @RequestBody Map<String, Object> body) {
        taskService.sendCompleteMessage(processId, body);
    }

}
