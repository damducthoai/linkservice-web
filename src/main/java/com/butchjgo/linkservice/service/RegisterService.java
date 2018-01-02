package com.butchjgo.linkservice.service;

public interface RegisterService<T> {
    void register(T t);

    void unregister(T t);
}
