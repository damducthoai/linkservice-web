package com.butchjgo.linkservice.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.Map;

@Service(value = "sseEmitterFactory")
public class SseEmitterFactoryImpl implements SseEmitterFactory {

    @Value("${ssemiter.timeout}")
    long timeout;

    @Resource(name = "emitterPool")
    Map<String, SseEmitter> emitterPool;

    public SseEmitterFactoryImpl() {
    }

    @Override
    public SseEmitter get() {
        SseEmitter emitter = new SseEmitter(timeout);

        emitter.onError(e -> remove(emitter));
        emitter.onTimeout(() -> remove(emitter));
        emitter.onCompletion(() -> remove(emitter));

        return emitter;
    }

    void remove(SseEmitter emitter) {
        System.out.println("removing one item");
        synchronized (emitterPool) {
            emitterPool.values().remove(emitter);
        }
    }
}
