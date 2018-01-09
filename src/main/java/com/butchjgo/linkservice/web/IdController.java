package com.butchjgo.linkservice.web;

import com.butchjgo.linkservice.service.SseEmitterFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@CrossOrigin
@Scope("session")
public class IdController {

    @Resource(name = "sseEmitterFactory")
    SseEmitterFactory sseEmitterFactory;

    @Resource(name = "emitterPool")
    Map<String, SseEmitter> emitterPool;


    @GetMapping(path = "id")
    String doGet(HttpSession session) {
        return session.getId();
    }

    @GetMapping(path = "result/{clientId}")
    SseEmitter sseEmitter(@PathVariable String clientId, HttpServletResponse response) {
        SseEmitter emitter = sseEmitterFactory.get();
        emitterPool.put(clientId, emitter);
        response.addHeader("Cache-control","no-cache");
        response.addHeader("X-Accel-Buffering","no");
        return emitterPool.get(clientId);
    }
}
