package com.butchjgo.linkservice.service;

public interface RequestPublisher<T> {
    void publish(T t);
}
