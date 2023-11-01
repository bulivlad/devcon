package io.dotinc.devcon.workers.controller;

import io.dotinc.devcon.workers.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vlabulim1 on 01.11.2023.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;
    @GetMapping(path="/sse", produces = "text/event-stream")
    public SseEmitter createConnection() {
        return sseService.init();
    }

}
