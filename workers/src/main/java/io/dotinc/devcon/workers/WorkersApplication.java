package io.dotinc.devcon.workers;

import io.camunda.operate.CamundaOperateClient;
import io.camunda.operate.auth.SelfManagedAuthentication;
import io.camunda.operate.exception.OperateException;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@EnableRetry
@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = {"classpath:workflow/*.bpmn", "classpath:workflow/*.dmn"})
public class WorkersApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkersApplication.class, args);
	}

	@Bean
	public CamundaOperateClient camundaOperateClient() throws OperateException {
		return new CamundaOperateClient.Builder().operateUrl("http://localhost:8081/")
				.authentication(new SelfManagedAuthentication("devcon", "InSmVm4i1F6wmzflXHv2LHCP3b7Jr1av"))
				.build();
	}

	@Bean
	public CamundaTaskListClient camundaTaskListClient() throws TaskListException {
		return new CamundaTaskListClient.Builder().taskListUrl("http://localhost:8082/")
				.shouldReturnVariables()
				.authentication(new io.camunda.tasklist.auth.SelfManagedAuthentication("devcon", "InSmVm4i1F6wmzflXHv2LHCP3b7Jr1av"))
				.build();
	}

	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public List<SseEmitter> sseEmitters() {
		return new ArrayList<>();
	}

}
