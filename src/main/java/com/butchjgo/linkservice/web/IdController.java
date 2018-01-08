package com.butchjgo.linkservice.web;

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
@CrossOrigin(origins = "http://13.228.29.177")
@Scope(value = "session")
public class IdController {

    String clientId;

    @Resource(name = "emitterPool")
    Map<String, SseEmitter> emitterPool;

    public IdController(HttpSession httpSession) {
        clientId = httpSession.getId();
    }

    @GetMapping(path = "id")
    String doGet(HttpServletResponse response, HttpSession  session) {
        response.addHeader("Access-Control-Allow-Credentials","true");
        return session.getId();
    }

    @GetMapping(path = "result/{clientId}")
    SseEmitter sseEmitter(@PathVariable String clientId, HttpServletResponse response) {
        SseEmitter emitter = new SseEmitter(60*60*1000L);

        emitterPool.put(clientId, emitter);
        response.addHeader("Access-Control-Allow-Credentials","true");

        return emitterPool.get(clientId);
    }
}
