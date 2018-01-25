package com.butchjgo.linkservice.web.pool;

public interface Pool<T> {
    boolean contain(T t);

    boolean isSupported(T t);
}
