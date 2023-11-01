package io.dotinc.devcon.frontend.controller;

import io.dotinc.devcon.frontend.dto.response.TaskResponse;
import io.dotinc.devcon.frontend.service.BackendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vlabulim1 on 01.11.2023.
 */

@Controller
@RequestMapping("/v2")
@RequiredArgsConstructor
public class IndexControllerV2 {
    private final BackendService backendService;

    @GetMapping("/")
    public String home() {
        return "/v2/index.html";
    }

    @GetMapping("/error")
    public String error() {
        return "/v2/error.html";
    }

    @PostMapping("/workflow/start")
    @ResponseStatus(HttpStatus.OK)
    public void workflowStart() throws Exception {
        backendService.startWorkflowV2();
    }

}
