package com.butchjgo.linkservice.common.pool;

import org.springframework.stereotype.Service;

@Service(value = "badURLPool")
public class BadURLPool implements Pool<String> {

    @Override
    public boolean contain(String s) {
        // TODO implement business logic here
        return false;
    }

    @Override
    public boolean isSupported(String s) {
        return false;
    }
}
