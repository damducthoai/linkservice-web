package com.butchjgo.linkservice.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterFactory {
    SseEmitter get();
}
