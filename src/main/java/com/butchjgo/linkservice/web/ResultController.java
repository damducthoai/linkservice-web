package com.butchjgo.linkservice.web;

import com.butchjgo.linkservice.web.service.SseEmitterFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@CrossOrigin
public class ResultController {

    @Resource(name = "sseEmitterFactory")
    SseEmitterFactory sseEmitterFactory;

    @Resource(name = "emitterPool")
    Map<String, SseEmitter> emitterPool;

    @GetMapping(path = "result")
    SseEmitter sseEmitter(HttpSession session) {
        String sessionId = session.getId();
        SseEmitter emitter = sseEmitterFactory.get();
        emitterPool.put(sessionId, emitter);
        return emitter;
    }
}
