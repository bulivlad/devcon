package io.dotinc.devcon.workers.service;

import io.camunda.operate.CamundaOperateClient;
import io.camunda.operate.dto.FlownodeInstance;
import io.camunda.operate.dto.FlownodeInstanceState;
import io.camunda.operate.dto.ProcessInstance;
import io.camunda.operate.dto.ProcessInstanceState;
import io.camunda.operate.search.Filter;
import io.camunda.operate.search.FlownodeInstanceFilter;
import io.camunda.operate.search.SearchQuery;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.dto.Form;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskList;
import io.camunda.tasklist.dto.TaskSearch;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.dto.Variable;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.instance.UserTask;
import io.dotinc.devcon.workers.dto.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author vlabulim1 on 22.10.2023.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final CamundaTaskListClient camundaTaskListClient;
    private final CamundaOperateClient camundaOperateClient;
    private final ZeebeClient zeebeClient;

    @Retryable(maxAttempts = 100, backoff = @Backoff(delay = 50))
    public TaskResponse getTask(String processId) throws Exception {
        TaskList tasks = camundaTaskListClient.getTasks(new TaskSearch().setProcessInstanceId(processId)
                .setState(TaskState.CREATED).setWithVariables(true));
        Task task = tasks.first();
        if (task == null) {
            ProcessInstance processInstance = camundaOperateClient.getProcessInstance(Long.valueOf(processId));
            if (processInstance.getState() != ProcessInstanceState.ACTIVE) {
                TaskResponse taskResponse = new TaskResponse();
                taskResponse.setIsLast(true);
                taskResponse.setIsCompleted(true);
                return taskResponse;
            } else {
                TaskList complete = camundaTaskListClient.getTasks(new TaskSearch().setProcessInstanceId(processId));
                UserTask[] modelElementsByType = camundaOperateClient
                        .getProcessDefinitionModel(processInstance.getProcessDefinitionKey())
                        .getModelElementsByType(UserTask.class).toArray(new UserTask[]{});
                if (complete.size() < modelElementsByType.length) {
                    throw new Exception("Cannot get task for process " + processId + ". Waiting to become active");
                }
                TaskResponse taskResponse = new TaskResponse();
                taskResponse.setIsLast(true);
                taskResponse.setIsCompleted(true);
                return taskResponse;
            }
        }
        Form form = camundaTaskListClient.getForm(task.getFormKey().replace("camunda-forms:bpmn:", ""), task.getProcessDefinitionId());

        List<Variable> variables = task.getVariables() != null ? task.getVariables() : new ArrayList<>();
        Optional<Variable> maybeIsFirst = variables.stream().filter(e -> "isLast".equals(e.getName())).findFirst();
        log.info("Task {} has variable present={} isLast={}", task.getName(), maybeIsFirst.isPresent(), maybeIsFirst.isPresent() ? maybeIsFirst.get().getValue() : false);
        return new TaskResponse(task.getId(), form.getSchema(), maybeIsFirst.isPresent() ? (Boolean) maybeIsFirst.get().getValue() : false, false, task.getProcessInstanceId());
    }

    @Retryable(maxAttempts = 50, backoff = @Backoff(delay = 50))
    public Task completeEndUserTask(String taskId, Map<String, Object> variables) throws Exception {
//        Task claimed = camundaTaskListClient.claim(taskId, "customer", true);
        return camundaTaskListClient.completeTask(taskId, variables);
    }

    public void sendCompleteMessage(String processId, Map<String, Object> variables) {
        Map<String, Object> safeVariables = variables != null ? variables : new HashMap<>();
        zeebeClient.newPublishMessageCommand()
                .messageName("StepComplete")
                .correlationKey(processId)
                .variables(safeVariables)
                .send()
                .join();
    }

}
