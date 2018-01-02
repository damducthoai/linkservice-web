package com.butchjgo.linkservice.common.pool;

import com.butchjgo.linkservice.service.RegisterService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service(value = "supportedURLPool")
public class SupportedURLPool implements Pool<String>, RegisterService<String> {

    Set<String> patternPool = new HashSet<>();

    @Override
    public boolean contain(String s) {
        return false;
    }

    @Override
    public boolean isSupported(final String url) {

        return patternPool.stream().anyMatch(p -> url.matches(p));
    }

    @Override
    public void register(String s) {
        patternPool.add(s);
    }

    @Override
    public void unregister(String s) {
        patternPool.remove(s);
    }
}
