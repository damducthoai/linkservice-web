package com.butchjgo.linkservice.common.pool;

public interface Pool<T> {
    boolean contain(T t);

    boolean isSupported(T t);
}
