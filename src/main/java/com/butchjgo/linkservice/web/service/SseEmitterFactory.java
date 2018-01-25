package com.butchjgo.linkservice.web.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterFactory {
    SseEmitter get();
}
