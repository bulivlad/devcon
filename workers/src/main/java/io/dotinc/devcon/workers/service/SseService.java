package io.dotinc.devcon.workers.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author vlabulim1 on 01.11.2023.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private List<SseEmitter> sseEmitters = new CopyOnWriteArrayList<>();

    public SseEmitter init() {
        log.info("init");
        SseEmitter emitter = new SseEmitter(-1L);
        emitter.onCompletion(() -> sseEmitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            sseEmitters.remove(emitter);
        });
        sseEmitters.add(emitter);
        log.info("size {}", sseEmitters.size());
        return emitter;
    }

    public void emit(Object event) {
        List<SseEmitter> failures = new CopyOnWriteArrayList<>();
        log.info("here + {}", sseEmitters.size());
        sseEmitters.stream()
                .filter(Objects::nonNull)
                .forEach(emitter -> {
                    try {
                        log.info("sent");
                        emitter.send(event);
                    } catch (Exception ex) {
                        log.info("failed");
                        emitter.completeWithError(ex);
                        failures.add(emitter);
                    }
                });
        if (!failures.isEmpty()) {
            log.error("failures");
            sseEmitters.removeAll(failures);
        }
    }
}
