package io.dotinc.devcon.frontend.controller;

import io.dotinc.devcon.frontend.dto.response.TaskResponse;
import io.dotinc.devcon.frontend.service.BackendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author vlabulim1 on 22.10.2023.
 */

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final BackendService backendService;

    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    @GetMapping("/error")
    public String error() {
        return "error.html";
    }

    @PostMapping("/workflow/start")
    public String workflowStart() throws Exception {
        String process = backendService.startWorkflow();
        return "redirect:/workflow/" + process;
    }

    @GetMapping("/workflow/{process}")
    public ModelAndView workflowStep(@PathVariable String process) throws Exception {
        TaskResponse taskResponse = backendService.getTask(process);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("workflow.html");
        modelAndView.addObject("form", taskResponse.getForm() == null ? "null" : taskResponse.getForm());
        modelAndView.addObject("isCompleted", taskResponse.getIsCompleted());
        modelAndView.addObject("isLast", taskResponse.getIsLast());
        modelAndView.addObject("taskId", taskResponse.getTaskId());
        modelAndView.addObject("processId", taskResponse.getProcessId());

        return modelAndView;
    }

    @GetMapping("/workflow/finish")
    public String finish() {
        return "finish.html";
    }
}
