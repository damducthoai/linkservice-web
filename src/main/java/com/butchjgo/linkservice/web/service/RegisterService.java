package com.butchjgo.linkservice.web.service;

public interface RegisterService<T> {
    void register(T t);

    void unregister(T t);
}
