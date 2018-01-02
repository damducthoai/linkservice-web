package com.butchjgo.linkservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RequestPublisher<T> {
    void publish(T t) throws JsonProcessingException;
}
